package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel

@Composable
fun FitnessScreen(viewModel: ReclaimViewModel) {
    val user by viewModel.userState.collectAsState()
    val workouts by viewModel.workoutsState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var inputTitle by remember { mutableStateOf("") }
    var inputDuration by remember { mutableStateOf("20") }
    var inputIntensity by remember { mutableStateOf("Med Intensity") } // Low, Med, High Intensity

    // Dynamic fitness logs math projection
    val totalLoggedMins = remember(workouts) {
        workouts.sumOf { it.durationMinutes } + 42
    }
    val totalLoggedCalories = remember(workouts) {
        (workouts.sumOf { it.durationMinutes } * 10) + 450
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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
                text = "Physical Vitality",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Optimize your physiology for cognitive performance and focus. Out-exercise your scroll habit.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Steps Goal Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.DirectionsRun,
                            contentDescription = "Steps",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DAILY STEPS TRACKER",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )
                    }

                    Text(
                        text = "84% Complete",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${user?.currentSteps ?: 8432}",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "/ 10,000 steps",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Steps progress bar
                val progressValue = (user?.currentSteps?.toFloat() ?: 8432f) / 10000f
                LinearProgressIndicator(
                    progress = { progressValue.coerceAtMost(1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = Color(0xFF10B981),
                    trackColor = BorderGray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Extra dynamic stats block: Calorie and Active mins grid rows
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ACTIVE MINUTES", style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalLoggedMins mins",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Text("Outdoors & gym logs", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("METABOLIC METRIC", style = MaterialTheme.typography.labelSmall, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$totalLoggedCalories kcal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Text("Daily active burn rate", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Today's workout completed history list header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WORKOUTS LOGGED TODAY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )

            TextButton(
                onClick = { showDialog = true },
                modifier = Modifier.testTag("log_workout_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Log Workout", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Render workout rows from Room Database
        Box(modifier = Modifier.weight(1f)) {
            if (workouts.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No workouts logged today. Tap Log Workout to out-exercise screen addiction!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(workouts) { w ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                            border = BorderStroke(1.dp, BorderGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DirectionsRun,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column {
                                        Text(
                                            text = w.title,
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                        Text(
                                            text = "${w.durationMinutes} mins  ·  ${w.intensity}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextGray
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = "Logged",
                                    tint = Color(0xFF10B981)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialogue
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Log Workout Activity",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text("WORKOUT TITLE", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = inputTitle,
                            onValueChange = { inputTitle = it },
                            placeholder = { Text("e.g. Mindful Walk, Calisthenics") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Column {
                        Text("DURATION (MINUTES)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = inputDuration,
                            onValueChange = { inputDuration = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Column {
                        Text("INTENSITY", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Low Intensity", "Med Intensity", "High Intensity").forEach { inten ->
                                val sel = inputIntensity == inten
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (sel) Color(0xFFD1FAE5) else ReclaimSurface)
                                        .border(1.dp, if (sel) Color(0xFF10B981) else BorderGray, RoundedCornerShape(8.dp))
                                        .clickable { inputIntensity = inten }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = inten.replace(" Intensity", ""),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (sel) Color(0xFF065F46) else TextDark
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mins = inputDuration.toIntOrNull() ?: 20
                        if (inputTitle.isNotBlank()) {
                            viewModel.logWorkout(inputTitle, mins, inputIntensity)
                            showDialog = false
                            inputTitle = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Log (+50 XP)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel", color = TextGray)
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = Color.White
        )
    }
}
