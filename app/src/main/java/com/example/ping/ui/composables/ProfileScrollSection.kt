package com.example.ping.ui.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlin.math.roundToInt
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect

private fun Dp.toPx(density: Density): Float = with(density) { toPx() }

object BubbleShape : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = 12.dp.toPx(density)
        val pointerWidth = 8.dp.toPx(density)
        val pointerHeight = 6.dp.toPx(density)
        
        val path = Path().apply {
            // Main bubble body
            addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = size.width,
                    bottom = size.height - pointerHeight,
                    topLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    topRightCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomRightCornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    bottomLeftCornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
            )
            
            // Pointer
            moveTo(size.width / 2 - pointerWidth / 2, size.height - pointerHeight)
            lineTo(size.width / 2, size.height)
            lineTo(size.width / 2 + pointerWidth / 2, size.height - pointerHeight)
            close()
        }
        
        return Outline.Generic(path)
    }
}

data class ProfileItem(
    val id: String,
    val name: String,
    val imageResId: Int,
    val bio: String = ""
)

@Composable
fun ProfileScrollSection(
    profiles: List<ProfileItem>,
    modifier: Modifier = Modifier
) {
    var selectedProfile by remember { mutableStateOf<ProfileItem?>(null) }

    AnimatedContent(
        targetState = selectedProfile,
        transitionSpec = {
            val animationSpec = spring<IntOffset>(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
            if (targetState != null) {
                slideInHorizontally(animationSpec = animationSpec, initialOffsetX = { fullWidth -> -fullWidth }) togetherWith
                        slideOutHorizontally(animationSpec = animationSpec, targetOffsetX = { fullWidth -> fullWidth })
            } else {
                slideInHorizontally(animationSpec = animationSpec, initialOffsetX = { fullWidth -> fullWidth }) togetherWith
                        slideOutHorizontally(animationSpec = animationSpec, targetOffsetX = { fullWidth -> -fullWidth })
            }
        },
        label = "ProfileSectionTransition",
        modifier = modifier.fillMaxSize()
    ) { targetProfile ->
        if (targetProfile == null) {
            // Display the scrollable list of profiles
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(profiles) { _, profile ->
                    Box(
                        modifier = Modifier
                            .onGloballyPositioned { _ ->
                                // Offset and width logic not needed here anymore
                            }
                    ) {
                        ProfileItemWithBubble(
                            profile = profile,
                            onBubbleClick = { selectedProfile = profile }
                        )
                    }
                }
            }
        } else {
            // Display the expanded profile view
            ExpandedProfileView(
                profile = targetProfile,
                onBackClick = { selectedProfile = null }
            )
        }
    }
}

@Composable
private fun ProfileItemWithBubble(
    profile: ProfileItem,
    onBubbleClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        // Speech bubble above profile
        if (profile.bio.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .wrapContentSize()
                    .clickable(onClick = onBubbleClick)
                    .width(100.dp)
                    .height(48.dp)
            ) {
                // Bubble with shadow and border
                Surface(
                    shape = BubbleShape,
                    shadowElevation = 2.dp,
                    color = Color.White,
                    modifier = Modifier
                        .width(100.dp)
                        .height(48.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.White,
                                shape = BubbleShape
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = profile.bio,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
        
        // Profile Picture with border
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6E0F8))
                .padding(2.dp)
                .background(Color.White, CircleShape)
                .padding(2.dp)
                .background(Color(0xFFE6E0F8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = profile.name.first().toString(),
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF6F4FC7),
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Username
        Text(
            text = profile.name,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpandedProfileView(
    profile: ProfileItem,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Picture and Name Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp) // Larger size for expanded view
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer), // A subtle background color
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.first().toString(),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer, // Contrasting text color
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(32.dp)) // Increased spacing for better visual separation

            // Bio/Notiz Section with bubble effect
            Text(
                text = "Notiz",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp), // More rounded corners for a modern look
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp), // Padding inside the card
                    textAlign = TextAlign.Start // Align text to the start for better readability
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScrollSectionPreview() {
    MaterialTheme {
        ProfileScrollSection(
            profiles = sampleProfiles,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private val sampleProfiles = listOf(
    ProfileItem(
        id = "1",
        name = "John",
        imageResId = 0,
        bio = "Welche Songs hast du auf deiner Playlist?"
    ),
    ProfileItem(
        id = "2",
        name = "Rosali",
        imageResId = 0,
        bio = "Hallo 👋"
    ),
    ProfileItem(
        id = "3",
        name = "Albert",
        imageResId = 0,
        bio = "I posted 🎁"
    ),
    ProfileItem(
        id = "4",
        name = "Vetmi",
        imageResId = 0,
        bio = "i ❤️ people"
    ),
    ProfileItem(
        id = "5",
        name = "Sarah",
        imageResId = 0,
        bio = "Let's create something amazing!"
    )
)