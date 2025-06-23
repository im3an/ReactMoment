package com.example.ping.ui.composables

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.graphics.vector.ImageVector

data class Reaction(
    val id: String,
    val icon: ImageVector,
    val color: Color,
    val count: Int = 0
)

@Composable
fun ReactionBar(
    reactions: List<Reaction>,
    onReactionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showReactionWheel by remember { mutableStateOf(false) }
    var selectedReaction by remember { mutableStateOf<Reaction?>(null) }

    Box(modifier = modifier) {
        // Reaction button
        IconButton(
            onClick = { showReactionWheel = !showReactionWheel },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddReaction,
                contentDescription = "Add Reaction",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Reaction wheel popup
        AnimatedVisibility(
            visible = showReactionWheel,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Popup(
                onDismissRequest = { showReactionWheel = false },
                properties = PopupProperties(focusable = true)
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(IntrinsicSize.Min),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        reactions.forEach { reaction ->
                            ReactionItem(
                                reaction = reaction,
                                onClick = {
                                    selectedReaction = reaction
                                    onReactionSelected(reaction.id)
                                    showReactionWheel = false
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        // Selected reactions display
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            reactions.filter { it.count > 0 }.forEach { reaction ->
                ReactionChip(
                    reaction = reaction,
                    onClick = { onReactionSelected(reaction.id) }
                )
            }
        }
    }
}

@Composable
private fun ReactionItem(
    reaction: Reaction,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(reaction.color.copy(alpha = 0.1f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = reaction.icon, contentDescription = reaction.id, tint = reaction.color)
    }
}

@Composable
private fun ReactionChip(
    reaction: Reaction,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
        color = reaction.color.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = reaction.icon, contentDescription = reaction.id, tint = reaction.color)
            Text(
                text = reaction.count.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReactionBarPreview() {
    MaterialTheme {
        val sampleReactions = listOf(
            Reaction(
                id = "like",
                icon = Icons.Default.Favorite,
                color = Color.Red,
                count = 5
            ),
            Reaction(
                id = "laugh",
                icon = Icons.Default.EmojiEmotions,
                color = Color.Yellow,
                count = 3
            ),
            Reaction(
                id = "wow",
                icon = Icons.Default.SentimentVerySatisfied,
                color = Color.Orange,
                count = 2
            )
        )

        ReactionBar(
            reactions = sampleReactions,
            onReactionSelected = { /* Handle reaction selection */ }
        )
    }
}
