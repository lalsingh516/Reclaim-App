package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel
import kotlinx.coroutines.delay

@Composable
fun WellnessScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val logs by viewModel.journalEntriesState.collectAsState()

    var activeState by remember { mutableStateOf("Calm") } // Locked, Focused, Calm, Stressed
    var journalInput by remember { mutableStateOf("") }
    var playingMeditationTitle by remember { mutableStateOf<String?>(null) }
    var meditationSecondsRemaining by remember { mutableStateOf(300) }
    var isMeditationPlaying by remember { mutableStateOf(false) }

    // Simulates playing mindfulness meditations
    LaunchedEffect(isMeditationPlaying, meditationSecondsRemaining) {
        if (isMeditationPlaying && meditationSecondsRemaining > 0) {
            delay(1000)
            meditationSecondsRemaining--
        } else if (meditationSecondsRemaining == 0) {
            isMeditationPlaying = false
            playingMeditationTitle = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // App Header Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo("main_dashboard") }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = PrimaryDark
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mental Clarity",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Your space for daily intentionality, quietness, and rest.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // State selectors: "How are you feeling today?"
        Text(
            text = "CURRENT MIND STATE",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val moodOptions = listOf(
                Pair("Focused", Icons.Default.FlashOn),
                Pair("Calm", Icons.Default.Spa),
                Pair("Stressed", Icons.Default.Air)
            )

            moodOptions.forEach { pair ->
                val active = activeState == pair.first
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (active) ReclaimIndigoLight else ReclaimSurface)
                        .border(1.dp, if (active) ReclaimIndigo else BorderGray, RoundedCornerShape(12.dp))
                        .clickable { activeState = pair.first }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first,
                            tint = if (active) ReclaimIndigo else TextGray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pair.first,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (active) ReclaimIndigo else TextDark
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Reflection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DAILY REFLECTION & GRATITUDE",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reflecting builds distance from immediate triggers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = journalInput,
                    onValueChange = { journalInput = it },
                    placeholder = { Text("How has screen discipline treated you today? Share a win...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ReclaimIndigo,
                        unfocusedBorderColor = BorderGray
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (journalInput.isNotBlank()) {
                            viewModel.logMoodAndReflection(activeState, journalInput)
                            journalInput = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark),
                    shape = RoundedCornerShape(8.dp),
                    enabled = journalInput.isNotBlank()
                ) {
                    Text("Save Reflection Log (+15 XP)")
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Audio Simulated soundscape player overlay when active
        if (playingMeditationTitle != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = PrimaryDark),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "NOW PLAYING",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = playingMeditationTitle ?: "",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        val remMins = meditationSecondsRemaining / 60
                        val remSecs = meditationSecondsRemaining % 60
                        Text(
                            text = String.format("%02d:%02d remaining", remMins, remSecs),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = { isMeditationPlaying = !isMeditationPlaying }
                        ) {
                            Icon(
                                imageVector = if (isMeditationPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                isMeditationPlaying = false
                                playingMeditationTitle = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Stop",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // Mindfulness Meditation Library
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MINDFULNESS MEDITATION LIBRARY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        listOf(
            Triple("5-min Reset", "Short breathwork session for a busy day.", 5),
            Triple("Deep Focus Soundscape", "Binaural beat sequences to stimulate neuro-clarity.", 15),
            Triple("Evening Wind-down", "Gentle guided sequence optimizing melatonin releases.", 10)
        ).forEach { meditation ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = meditation.first,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = meditation.second,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${meditation.third} min",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = ReclaimIndigo
                        )
                    }

                    IconButton(
                        onClick = {
                            playingMeditationTitle = meditation.first
                            meditationSecondsRemaining = meditation.third * 60
                            isMeditationPlaying = true
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ReclaimIndigoLight)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = ReclaimIndigo
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Log of prior day journal reflections
        Text(
            text = "REFLECTIONS LOG HISTORY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (logs.isEmpty()) {
            Text(
                text = "Reflections ledger is empty. Complete your first journal above!",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        } else {
            for (entry in logs) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                    border = BorderStroke(1.dp, BorderGray)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            contentDescription = entry.mood,
                            tint = ReclaimIndigo,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "State: ${entry.mood}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = entry.reflection,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextDark
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}
