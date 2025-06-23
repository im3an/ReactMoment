import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ping.R
import com.example.ping.ui.composables.ReactionBar
import com.example.ping.ui.composables.Reaction

@Composable
fun ChallengeCard(
    title: String,
    description: String,
    imagePainter: Painter,
    timeRemaining: String = "1:45",
    userResponded: Boolean = false,
    friendsResponded: Int = 3,
    totalFriends: Int = 5,
    onClick: () -> Unit = {}
) {
    var reactions by remember {
        mutableStateOf(
            listOf(
                Reaction(
                    id = "like",
                    icon = Icons.Default.Favorite,
                    color = Color.Red,
                    count = 0
                ),
                Reaction(
                    id = "laugh",
                    icon = Icons.Default.EmojiEmotions,
                    color = Color(0xFFFFCA28),
                    count = 0
                ),
                Reaction(
                    id = "wow",
                    icon = Icons.Default.SentimentVerySatisfied,
                    color = Color(0xFFFF9800),
                    count = 0
                )
            )
        )
    }

    // Animation states
    var isPressed by remember { mutableStateOf(false) }
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }

    // Card colors with smooth transition
    val cardColors = if (userResponded) {
        CardDefaults.cardColors(containerColor = Color(0xFFF0F7FF))
    } else {
        CardDefaults.cardColors(containerColor = Color.White)
    }

    // Heartbeat animation for non-responded cards
    val infiniteTransition = rememberInfiniteTransition(label = "heartbeat")
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (!userResponded) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = EaseInOutCubic
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartbeatScale"
    )

    // Click animation
    val clickScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "clickScale"
    )

    // Glow effect for non-responded cards
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = if (!userResponded) 0.3f else 0f,
        targetValue = if (!userResponded) 0.7f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = EaseInOutSine
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Card(
        modifier = Modifier
            .width(300.dp)
            .height(380.dp)
            .padding(8.dp)
            .graphicsLayer {
                scaleX = heartbeatScale * clickScale
                scaleY = heartbeatScale * clickScale
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null // We handle our own visual feedback
            ) {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPressed) 12.dp else 6.dp,
            pressedElevation = 12.dp
        ),
        colors = cardColors,
        border = if (!userResponded) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha)
            )
        } else {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.Green.copy(alpha = 0.5f)
            )
        }
    ) {
        // Detect press state
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is androidx.compose.foundation.interaction.PressInteraction.Press -> {
                        isPressed = true
                    }
                    is androidx.compose.foundation.interaction.PressInteraction.Release,
                    is androidx.compose.foundation.interaction.PressInteraction.Cancel -> {
                        isPressed = false
                    }
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Card Header with Challenge Status
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Overlay for time remaining with fade animation
                val timeOverlayAlpha by animateFloatAsState(
                    targetValue = if (!userResponded) 1f else 0.7f,
                    animationSpec = tween(300),
                    label = "timeOverlayAlpha"
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x99000000).copy(alpha = timeOverlayAlpha))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = timeRemaining,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Challenge Button with animation
                if (!userResponded) {
                    val buttonScale by animateFloatAsState(
                        targetValue = if (isPressed) 1.1f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessHigh
                        ),
                        label = "buttonScale"
                    )

                    Button(
                        onClick = {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onClick()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .scale(buttonScale),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reagieren")
                        }
                    }
                } else {
                    // "Completed" badge with slide-in animation
                    val completeBadgeScale by animateFloatAsState(
                        targetValue = 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        ),
                        label = "completeBadgeScale"
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .scale(completeBadgeScale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF4CAF50))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Erledigt!",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Challenge Title with subtle animation
            val titleAlpha by animateFloatAsState(
                targetValue = if (userResponded) 0.8f else 1f,
                animationSpec = tween(300),
                label = "titleAlpha"
            )

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black.copy(alpha = titleAlpha),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Challenge Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.DarkGray.copy(alpha = if (userResponded) 0.7f else 1f),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Reaction Bar with entrance animation
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(600, delayMillis = 200)
                ) + fadeIn(animationSpec = tween(600, delayMillis = 200))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    ReactionBar(
                        reactions = reactions,
                        onReactionSelected = { reactionId ->
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            reactions = reactions.map { reaction ->
                                if (reaction.id == reactionId) {
                                    reaction.copy(count = reaction.count + 1)
                                } else {
                                    reaction
                                }
                            }
                        }
                    )
                }
            }

            // Friends Progress with animated progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                val animatedProgress by animateFloatAsState(
                    targetValue = friendsResponded.toFloat() / totalFriends.toFloat(),
                    animationSpec = tween(
                        durationMillis = 1000,
                        easing = EaseOutCubic
                    ),
                    label = "progressAnimation"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$friendsResponded von $totalFriends Freunden haben reagiert",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChallengeCardPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChallengeCard(
                title = "Zeig dein Frühstück!",
                description = "Mach ein Foto von deinem heutigen Frühstück - sei kreativ mit der Präsentation!",
                imagePainter = painterResource(id = R.drawable.image),
                userResponded = false,
                friendsResponded = 3,
                totalFriends = 7
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChallengeCard(
                title = "Verrücktes Selfie",
                description = "Mach ein Selfie an einem ungewöhnlichen Ort - überrasche deine Freunde!",
                imagePainter = painterResource(id = R.drawable.image2),
                userResponded = true,
                friendsResponded = 7,
                totalFriends = 7
            )
        }
    }
}