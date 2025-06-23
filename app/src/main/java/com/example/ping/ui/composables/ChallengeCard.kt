package com.example.ping.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ChallengeCard(
    title: String,
    description: String,
    imagePainter: androidx.compose.ui.graphics.painter.Painter,
    timeRemaining: String,
    userResponded: Boolean,
    friendsResponded: Int,
    totalFriends: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var reactions by remember {
        mutableStateOf(
            listOf(
                Reaction(
                    id = "like",
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.Red) },
                    color = Color.Red,
                    count = 0
                ),
                Reaction(
                    id = "laugh",
                    icon = { Icon(Icons.Default.EmojiEmotions, contentDescription = "Laugh", tint = Color.Yellow) },
                    color = Color.Yellow,
                    count = 0
                ),
                Reaction(
                    id = "wow",
                    icon = { Icon(Icons.Default.SentimentVerySatisfied, contentDescription = "Wow", tint = Color(0xFFFF9800)) },
                    color = Color(0xFFFF9800),
                    count = 0
                )
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Challenge Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                androidx.compose.foundation.Image(
                    painter = imagePainter,
                    contentDescription = title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Challenge Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Challenge Description
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Time Remaining and Friends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Time Remaining
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Timer,
                        contentDescription = "Time Remaining",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = timeRemaining,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Friends Responded
                Text(
                    text = "$friendsResponded/$totalFriends friends responded",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reaction Bar
            ReactionBar(
                reactions = reactions,
                onReactionSelected = { reactionId ->
                    reactions = reactions.map { reaction ->
                        if (reaction.id == reactionId) {
                            reaction.copy(count = reaction.count + 1)
                        } else {
                            reaction
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Respond Button
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (userResponded) 
                        MaterialTheme.colorScheme.secondary 
                    else 
                        MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = if (userResponded) "Responded" else "Respond to Challenge",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChallengeCardPreview() {
    MaterialTheme {
        ChallengeCard(
            title = "Sample Challenge",
            description = "This is a sample challenge description that might be a bit longer to show how it handles multiple lines of text.",
            imagePainter = painterResource(id = android.R.drawable.ic_menu_gallery),
            timeRemaining = "2:30",
            userResponded = false,
            friendsResponded = 3,
            totalFriends = 7,
            onClick = {}
        )
    }
} 