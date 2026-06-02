package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel

@Composable
fun FocusScreen(viewModel: ReclaimViewModel) {
    var screenSegment by remember { mutableStateOf("Timer") } // "Timer" vs "Analytics"
    val analyticsSeg by viewModel.analyticsSegment.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Heading
        Text(
            text = "Digital Discipline",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Text(
            text = "Enter deep work states and analyze scroll patterns.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Custom Switch Segment Control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ReclaimSurface)
                .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            listOf("Timer", "Analytics").forEach { seg ->
                val isActive = screenSegment == seg
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isActive) Color.White else Color.Transparent)
                        .clickable { screenSegment = seg }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = seg,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) PrimaryDark else TextGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (screenSegment == "Timer") {
            FocusTimerSection(viewModel)
        } else {
            FocusAnalyticsSection(viewModel, analyticsSeg)
        }
    }
}

@Composable
fun FocusTimerSection(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val timerSeconds by viewModel.timerSecondsRemaining.collectAsState()
    val timerState by viewModel.timerState.collectAsState()
    val rawTaskText by viewModel.timerActiveTask.collectAsState()

    var taskInputText by remember { mutableStateOf(rawTaskText) }
    var isEditingTask by remember { mutableStateOf(false) }

    val formattedTime = remember(timerSeconds) {
        val minutes = timerSeconds / 60
        val seconds = timerSeconds % 60
        String.format("%02d:%02d", minutes, seconds)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Large Custom Circle Progress timer
        Box(
            modifier = Modifier
                .size(260.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 12.dp.toPx()
                // Inactive grey circle background
                drawCircle(
                    color = BorderGray,
                    radius = (size.minDimension - strokeWidth) / 2,
                    style = Stroke(width = strokeWidth)
                )

                // Active indigo countdown section
                val progressAngle = (timerSeconds.toFloat() / 1500f) * 360f // based on 25 mins base
                drawArc(
                    color = ReclaimIndigo,
                    startAngle = -90f,
                    sweepAngle = progressAngle,
                    useCenter = false,
                    size = Size(
                        width = size.width - strokeWidth,
                        height = size.height - strokeWidth
                    ),
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    style = Stroke(width = strokeWidth)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (timerState == "running") "Deep Focus State" else "Ready to Focus",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = if (timerState == "running") ReclaimIndigo else TextGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Timer Controls row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stop button
            IconButton(
                onClick = { viewModel.stopTimer() },
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .border(BorderStroke(1.dp, BorderGray), CircleShape)
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Default.Stop,
                    contentDescription = "Stop",
                    tint = PrimaryDark,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            // Start/Pause Button (Primary)
            IconButton(
                onClick = {
                    if (timerState == "running") {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.startTimer()
                    }
                },
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PrimaryDark)
            ) {
                Icon(
                    imageVector = if (timerState == "running") Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (timerState == "running") "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Preset durations list
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            listOf(5, 15, 25, 45, 60).forEach { mins ->
                val isSelected = timerSeconds == mins * 60
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) ReclaimIndigoLight else ReclaimSurface)
                        .border(
                            1.dp,
                            if (isSelected) ReclaimIndigo else BorderGray,
                            RoundedCornerShape(20.dp)
                        )
                        .clickable { viewModel.setTimerDuration(mins) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${mins}m",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) ReclaimIndigo else TextDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Task name and input field box
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CURRENT TASK TARGET",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextGray,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {
                            if (isEditingTask) {
                                viewModel.changeActiveTask(taskInputText)
                            }
                            isEditingTask = !isEditingTask
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isEditingTask) Icons.Default.Check else Icons.Outlined.Edit,
                            contentDescription = "Edit",
                            tint = ReclaimIndigo,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                if (isEditingTask) {
                    OutlinedTextField(
                        value = taskInputText,
                        onValueChange = { taskInputText = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ReclaimIndigo,
                            unfocusedBorderColor = BorderGray
                        )
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = rawTaskText,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDark
                        )
                    }
                    Text(
                        text = "Session 2 of 4  ·  Automatic focus tracking enabled.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray,
                        modifier = Modifier.padding(start = 28.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FocusAnalyticsSection(viewModel: ReclaimViewModel, activeSeg: String) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        // Analytics segments (Day, Week, Month)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Day", "Week", "Month").forEach { item ->
                val active = activeSeg == item
                FilterChip(
                    selected = active,
                    onClick = { viewModel.setAnalyticsSegment(item) },
                    label = { Text(item) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryDark,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom drawn Screen Time comparative bar chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DAILY RECLAIMED CONTENT COMPARATIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextGray,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "2.8 hrs",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "reclaimed / day (average)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Canvas Native Bar Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barData = listOf(3.2f, 2.5f, 4.1f, 1.8f, 3.5f, 1.2f, 2.0f) // hours spent on social apps
                        val days = listOf("M", "T", "W", "T", "F", "S", "S")

                        val barWidth = 24.dp.toPx()
                        val spaceBetween = (size.width - (barWidth * barData.size)) / (barData.size + 1)
                        val maxVal = 5.0f

                        for (i in barData.indices) {
                            val score = barData[i]
                            val barHeight = (score / maxVal) * (size.height * 0.8f)
                            val xOffset = spaceBetween + i * (barWidth + spaceBetween)
                            val yOffset = size.height * 0.85f - barHeight

                            // Draw rounded rectangle bars
                            drawRoundRect(
                                color = if (i == 4) ReclaimIndigo else PrimaryDark.copy(alpha = 0.4f),
                                topLeft = Offset(xOffset, yOffset),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                            )
                        }

                        // Bottom grey baseline
                        drawLine(
                            color = BorderGray,
                            start = Offset(0f, size.height * 0.85f),
                            end = Offset(size.width, size.height * 0.85f),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }

                // Days Labels row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("M", "T", "W", "T", "F", "S", "S").forEach { d ->
                        Text(
                            text = d,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = TextGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Comparative Stats Blocks
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = ReclaimIndigo)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Deep Focus", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("84 / 100", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Moderate-High caliber", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Lightbulb, contentDescription = null, tint = Color(0xFFF59E0B))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pickups Checked", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("42 times", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("20% lower than avg", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // MOST USED SOCIAL APPS LIST (Distraction Vortex)
        Text(
            text = "MOST USED SOCIAL VORTEXES TODAY",
            style = MaterialTheme.typography.labelSmall,
            color = TextGray,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        VortexAppTile(title = "Instagram Reels", duration = "1h 45m", color = Color(0xFFEF4444))
        Spacer(modifier = Modifier.height(10.dp))
        VortexAppTile(title = "TikTok Feed", duration = "1h 12m", color = Color(0xFFF59E0B))
        Spacer(modifier = Modifier.height(10.dp))
        VortexAppTile(title = "Facebook Reels", duration = "35m", color = Color(0xFFFBBF24))
        Spacer(modifier = Modifier.height(10.dp))
        VortexAppTile(title = "YouTube Shorts", duration = "24m", color = TextGray)

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun VortexAppTile(title: String, duration: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
        border = BorderStroke(1.dp, BorderGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Text(
                text = duration,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (color == Color(0xFFEF4444)) Color(0xFFEF4444) else TextDark
            )
        }
    }
}
