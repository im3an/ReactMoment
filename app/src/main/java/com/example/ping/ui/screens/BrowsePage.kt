package com.example.ping.ui.screens

import ChallengeCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ping.ui.theme.PingTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import com.example.ping.R
import com.example.ping.ui.composables.ProfileScrollSection
import com.example.ping.ui.composables.ProfileItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowsePage(
    modifier: Modifier = Modifier,
    onChallengeClick: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Browse") },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                ),
//                actions = {
//                    IconButton(onClick = { /* TODO: Handle notifications */ }) {
//                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
//                    }
//                }
//            )
//        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Scroll Section
            item {
                ProfileScrollSection(
                    profiles = sampleProfiles,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Daily Progress Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Today's Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = 0.6f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "3 of 5 challenges completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Challenges Section
            item {
                Text(
                    "Today's Challenges",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Challenge Cards
            items(sampleChallenges) { challenge ->
                ChallengeCard(
                    title = challenge.title,
                    description = challenge.description,
                    imagePainter = painterResource(id = challenge.imageResId),
                    timeRemaining = challenge.timeRemaining,
                    userResponded = challenge.userResponded,
                    friendsResponded = challenge.friendsResponded,
                    totalFriends = challenge.totalFriends,
                    onClick = { onChallengeClick(challenge.id) }
                )
            }
        }
    }
}

// Sample data
data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val timeRemaining: String,
    val userResponded: Boolean,
    val friendsResponded: Int,
    val totalFriends: Int
)

private val sampleChallenges = listOf(
    Challenge(
        id = "1",
        title = "Zeig dein Frühstück!",
        description = "Mach ein Foto von deinem heutigen Frühstück - sei kreativ mit der Präsentation!",
        imageResId = R.drawable.image,
        timeRemaining = "2:30",
        userResponded = false,
        friendsResponded = 3,
        totalFriends = 7
    ),
    Challenge(
        id = "2",
        title = "Verrücktes Selfie",
        description = "Mach ein Selfie an einem ungewöhnlichen Ort - überrasche deine Freunde!",
        imageResId = R.drawable.image2,
        timeRemaining = "1:45",
        userResponded = true,
        friendsResponded = 7,
        totalFriends = 7
    ),
    Challenge(
        id = "3",
        title = "Kreative Kunst",
        description = "Zeige deine künstlerische Seite - male oder zeichne etwas Einzigartiges!",
        imageResId = R.drawable.image,
        timeRemaining = "4:15",
        userResponded = false,
        friendsResponded = 2,
        totalFriends = 7
    )
)

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

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun BrowsePageLightPreview() {
    PingTheme {
        BrowsePage()
    }
}