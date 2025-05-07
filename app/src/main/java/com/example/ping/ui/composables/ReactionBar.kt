package com.example.ping.ui.composables

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

data class EmojiReaction(
    val emoji: String,
    val label: String,
    val color: Color
)

@Composable
fun RadialReactionBar(
    onReactionSelected: (String) -> Unit = {}
) {
    val reactions = remember {
        listOf(
            EmojiReaction("❤️", "Love", Color(0xFFFF5252)),
            EmojiReaction("👍", "Like", Color(0xFF4CAF50)),
            EmojiReaction("😂", "Haha", Color(0xFFFFCA28)),
            EmojiReaction("😮", "Wow", Color(0xFF2196F3)),
            EmojiReaction("😢", "Sad", Color(0xFF9C27B0)),
            EmojiReaction("😡", "Angry", Color(0xFFFF9800))
        )
    }

    var componentSize by remember { mutableStateOf(IntSize.Zero) }
    var showReactionWheel by remember { mutableStateOf(false) }
    var wheelPosition by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(-1) }
    var currentPosition by remember { mutableStateOf(Offset.Zero) }

    val animatedScale by animateFloatAsState(
        targetValue = if (showReactionWheel) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "wheelScale"
    )

    val animatedAlpha by animateFloatAsState(
        targetValue = if (showReactionWheel) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "wheelAlpha"
    )

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .onSizeChanged { componentSize = it }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { position ->
                        wheelPosition = position
                        showReactionWheel = true
                    }
                )
            }
    ) {
        // Background dimmer when wheel is visible
        if (showReactionWheel) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.7f)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            showReactionWheel = false
                            selectedIndex = -1
                        }
                    }
            )
        }

        // Floating reaction bar
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                reactions.forEach { reaction ->
                    var emojiPosition by remember { mutableStateOf(Offset.Zero) }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .onGloballyPositioned { coordinates ->
                                emojiPosition = coordinates.positionInRoot()
                            }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        // Use the button's position + half its size to center wheel
                                        val centerX = emojiPosition.x + 20.dp.toPx()
                                        val centerY = emojiPosition.y + 20.dp.toPx()
                                        wheelPosition = Offset(centerX, centerY)
                                        showReactionWheel = true
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = reaction.emoji,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }

        // Reaction Wheel
        if (showReactionWheel) {
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(
                        x = with(LocalDensity.current) {
                            (wheelPosition.x - 150.dp.toPx()).toDp()
                        },
                        y = with(LocalDensity.current) {
                            (wheelPosition.y - 150.dp.toPx()).toDp()
                        }
                    )
                    .scale(animatedScale)
                    .alpha(animatedAlpha)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                isDragging = true
                                currentPosition = offset
                                selectedIndex = calculateSelectedIndex(offset, size.width.toFloat(), reactions.size)
                            },
                            onDragEnd = {
                                isDragging = false
                                if (selectedIndex >= 0 && selectedIndex < reactions.size) {
                                    onReactionSelected(reactions[selectedIndex].emoji)
                                    scope.launch {
                                        delay(200)
                                        showReactionWheel = false
                                    }
                                }
                            },
                            onDragCancel = {
                                isDragging = false
                            },
                            onDrag = { change, _ ->
                                currentPosition = change.position
                                selectedIndex = calculateSelectedIndex(
                                    change.position,
                                    size.width.toFloat(),
                                    reactions.size
                                )
                            }
                        )
                    }
            ) {
                // Draw the wheel
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2 * 0.85f
                    val innerRadius = radius * 0.4f

                    // Draw outer circle
                    drawCircle(
                        color = Color(0xFF333333).copy(alpha = 0.8f),
                        radius = radius,
                        center = center
                    )

                    // Draw inner circle
                    drawCircle(
                        color = Color(0xFF555555).copy(alpha = 0.8f),
                        radius = innerRadius,
                        center = center
                    )

                    // Draw dividers
                    val sectionAngle = 360f / reactions.size
                    for (i in 0 until reactions.size) {
                        val startAngle = i * sectionAngle - 90 // Start from top

                        // Draw section background (highlighted if selected)
                        val sectionColor = if (i == selectedIndex) {
                            reactions[i].color.copy(alpha = 0.5f)
                        } else {
                            Color.Transparent
                        }

                        drawArc(
                            color = sectionColor,
                            startAngle = startAngle,
                            sweepAngle = sectionAngle,
                            useCenter = true,
                            topLeft = Offset(center.x - radius, center.y - radius),
                            size = Size(radius * 2, radius * 2)
                        )

                        // Draw divider lines
                        val angleInRadians = Math.toRadians((startAngle).toDouble())
                        val lineStart = Offset(
                            x = center.x + (innerRadius * cos(angleInRadians)).toFloat(),
                            y = center.y + (innerRadius * sin(angleInRadians)).toFloat()
                        )
                        val lineEnd = Offset(
                            x = center.x + (radius * cos(angleInRadians)).toFloat(),
                            y = center.y + (radius * sin(angleInRadians)).toFloat()
                        )

                        drawLine(
                            color = Color.White.copy(alpha = 0.3f),
                            start = lineStart,
                            end = lineEnd,
                            strokeWidth = 2f
                        )
                    }
                }

                // Draw emojis on the wheel
                Box(modifier = Modifier.fillMaxSize()) {
                    val centerX = 150.dp
                    val centerY = 150.dp
                    val orbitRadius = 90.dp

                    reactions.forEachIndexed { index, reaction ->
                        val angle = (index * (360f / reactions.size) - 90) * (PI / 180f).toFloat()
                        val x = centerX + (orbitRadius * cos(angle))
                        val y = centerY + (orbitRadius * sin(angle))

                        val isSelected = index == selectedIndex
                        val emojiScale = if (isSelected) 1.3f else 1f

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .offset(
                                    x = x - 25.dp,
                                    y = y - 25.dp
                                )
                                .scale(emojiScale)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) reaction.color.copy(alpha = 0.8f)
                                    else Color(0xFF444444)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = reaction.emoji,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Center indicator
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color(0xFF222222)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedIndex >= 0 && selectedIndex < reactions.size) {
                            Text(
                                text = reactions[selectedIndex].emoji,
                                fontSize = 30.sp
                            )
                        } else {
                            Text(
                                text = "👆",
                                fontSize = 30.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun calculateSelectedIndex(position: Offset, size: Float, itemCount: Int): Int {
    val center = Offset(size / 2, size / 2)
    val vector = Offset(
        position.x - center.x,
        position.y - center.y
    )

    // If near center, no selection
    val distance = sqrt(vector.x * vector.x + vector.y * vector.y)
    if (distance < size * 0.2f) {
        return -1
    }

    // Calculate angle in degrees (0° is right, going clockwise)
    var angle = atan2(vector.y, vector.x) * (180 / PI).toFloat()
    if (angle < 0) angle += 360 // Convert to 0-360

    // Adjust angle so 0° is at the top
    angle = (angle + 90) % 360

    // Calculate section
    val sectionAngle = 360f / itemCount
    return (angle / sectionAngle).toInt()
}

@Preview(showBackground = true)
@Composable
fun RadialReactionBarPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF5F5F5)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                RadialReactionBar { selectedEmoji ->
                    println("Selected emoji: $selectedEmoji")
                }
            }
        }
    }
}
