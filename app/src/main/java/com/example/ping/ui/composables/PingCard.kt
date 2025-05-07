import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ping.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val cardColors = if (userResponded) {
        CardDefaults.cardColors(
            containerColor = Color(0xFFF0F7FF)
        )
    } else {
        CardDefaults.cardColors(
            containerColor = Color.White
        )
    }

    var pulseAnimation by remember { mutableStateOf(false) }
    val animatedScale = animateFloatAsState(
        targetValue = if (pulseAnimation) 1.05f else 1f,
        label = "scaleAnimation"
    )

    LaunchedEffect(key1 = Unit) {
        while (!userResponded) {
            pulseAnimation = true
            delay(2000)
            pulseAnimation = false
            delay(2000)
        }
    }

    Card(
        modifier = Modifier
            .width(300.dp)
            .height(320.dp)
            .padding(8.dp)
            .clickable { onClick() }
            .graphicsLayer {
                scaleX = animatedScale.value
                scaleY = animatedScale.value
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp
        ),
        colors = cardColors
    ) {
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

                // Overlay for time remaining
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0x99000000))
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

                // Challenge Button
                if (!userResponded) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x99000000))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Erledigt!",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Challenge Title
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Challenge Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Friends Progress
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Friend Response Indicator
                LinearProgressIndicator(
                    progress = friendsResponded.toFloat() / totalFriends.toFloat(),
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

@Composable
@Preview(showBackground = true)
fun ReactionGridPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF5F5F5)
    ) {

    }
}