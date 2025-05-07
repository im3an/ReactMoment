import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ping.R
import com.example.ping.ui.composables.RadialReactionBar
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val expiresAt: LocalDateTime,
    val userResponded: Boolean,
    val friendsResponded: Int,
    val totalFriends: Int
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    challenges: List<Challenge> = sampleChallenges
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ReactMoment",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Benachrichtigungen",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Group, contentDescription = "Freunde") },
                    label = { Text("Freunde") },
                    selected = false,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
                    label = { Text("Profil") },
                    selected = false,
                    onClick = { }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            if (challenges.isEmpty()) {
                EmptyStateView()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(challenges) { challenge ->
                        val timeRemaining = calculateTimeRemaining(challenge.expiresAt)

                        ChallengeCard(
                            title = challenge.title,
                            description = challenge.description,
                            imagePainter = painterResource(id = challenge.imageResId),
                            timeRemaining = timeRemaining,
                            userResponded = challenge.userResponded,
                            friendsResponded = challenge.friendsResponded,
                            totalFriends = challenge.totalFriends,
                            onClick = {
                                if (!challenge.userResponded) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Kamera öffnet sich...")
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Du hast diese Challenge bereits abgeschlossen!")
                                    }
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            // FAB for creating a custom challenge
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Neue Challenge erstellen...")
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Neue Challenge"
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResponseScreen(
    challenge: Challenge = sampleChallenges.first(),
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var showCameraView by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(challenge.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Challenge Description
            Text(
                text = challenge.description,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Camera View / Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF444444)),
                contentAlignment = Alignment.Center
            ) {
                if (showCameraView) {
                    // Hier würde die echte Kamera-Vorschau angezeigt
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                } else {
                    // Hier würde das aufgenommene Bild angezeigt
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Vorschau des aufgenommenen Bildes",
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (showCameraView) {
                    Button(
                        onClick = { showCameraView = false },
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Foto aufnehmen"
                        )
                    }
                } else {
                    // Buttons when preview is shown
                    Button(
                        onClick = { showCameraView = true },
                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray
                        )
                    ) {
                        Text("Neu aufnehmen")
                    }

                    Button(
                        onClick = onSubmit,
                        modifier = Modifier.weight(1f).padding(start = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Teilen")
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Keine aktiven Challenges",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Die nächste Challenge wird bald verfügbar sein. Bleib dran!",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsResponsesScreen(
    challenge: Challenge = sampleChallenges.first()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reaktionen") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Zurück"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = challenge.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${challenge.friendsResponded} von ${challenge.totalFriends} Freunden haben reagiert",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Hier würden die Reaktionen der Freunde angezeigt werden
            // Dies ist nur ein Beispiel
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Reaktionen auf deine Antwort:",
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    RadialReactionBar()
                }
            }
        }
    }
}

// Helper functions
@RequiresApi(Build.VERSION_CODES.O)
private fun calculateTimeRemaining(expiresAt: LocalDateTime): String {
    val now = LocalDateTime.now()
    val hours = java.time.Duration.between(now, expiresAt).toHours()
    val minutes = java.time.Duration.between(now, expiresAt).toMinutes() % 60

    return if (hours > 0) {
        "$hours:${minutes.toString().padStart(2, '0')}"
    } else {
        "0:${minutes.toString().padStart(2, '0')}"
    }
}

// Sample data
@RequiresApi(Build.VERSION_CODES.O)
val sampleChallenges = listOf(
    Challenge(
        id = "1",
        title = "Zeig dein Frühstück!",
        description = "Mach ein Foto von deinem heutigen Frühstück - sei kreativ mit der Präsentation!",
        imageResId = R.drawable.image,
        expiresAt = LocalDateTime.now().plusHours(1).plusMinutes(45),
        userResponded = false,
        friendsResponded = 3,
        totalFriends = 7
    ),
    Challenge(
        id = "2",
        title = "Verrücktes Selfie",
        description = "Mach ein Selfie an einem ungewöhnlichen Ort - überrasche deine Freunde!",
        imageResId = R.drawable.image2,
        expiresAt = LocalDateTime.now().plusMinutes(45),
        userResponded = true,
        friendsResponded = 7,
        totalFriends = 7
    ),
    Challenge(
        id = "3",
        title = "Dein aktueller Blick",
        description = "Zeige, was du gerade durch dein Fenster siehst.",
        imageResId = R.drawable.image2,
        expiresAt = LocalDateTime.now().plusHours(3).plusMinutes(15),
        userResponded = false,
        friendsResponded = 2,
        totalFriends = 7
    )
)

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ResponseScreenPreview() {
    MaterialTheme {
        ResponseScreen()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun FriendsResponsesScreenPreview() {
    MaterialTheme {
        FriendsResponsesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyStateViewPreview() {
    MaterialTheme {
        EmptyStateView()
    }
}