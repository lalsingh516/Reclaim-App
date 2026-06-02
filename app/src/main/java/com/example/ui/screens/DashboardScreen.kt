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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HabitEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel

@Composable
fun DashboardScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val user by viewModel.userState.collectAsState()
    val habits by viewModel.habitsState.collectAsState()

    val completedCount = habits.count { it.completedToday }
    val totalCount = habits.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // App Launcher Icon, Premium paywall link and Admin Panel link
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(ReclaimIndigoLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = ReclaimIndigo
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RECLAIM",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = ReclaimIndigo
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Admin control button
                IconButton(
                    onClick = { viewModel.navigateTo("admin_panel") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AdminPanelSettings,
                        contentDescription = "Admin settings",
                        tint = TextDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Premium paywall badge
                Box(
                    modifier = Modifier
                        .background(
                            if (user?.isPremium == true) Color(0xFFFEF3C7) else ReclaimIndigoLight,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            1.dp,
                            if (user?.isPremium == true) Color(0xFFF59E0B) else ReclaimIndigo,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { viewModel.navigateTo("paywall_screen") }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (user?.isPremium == true) "PRO" else "UPGRADE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (user?.isPremium == true) Color(0xFFB45309) else ReclaimIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Heading
        Text(
            text = "Good morning.",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark
        )
        Text(
            text = "Stay disciplined. Stay focused.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Intercept Button / Blocker Trigger! Frame blocker mode right at top to encourage mindfulness
        Card(
            onClick = { viewModel.navigateTo("replace_reels") },
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = PrimaryDark),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Block,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ACTIVE INTERCEPTOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Replace scrolling with wellness instantly",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Addiction Score Card with custom Drawn Bezier Graph (spline chart)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Text(
                        text = "ADDICTION SCORE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "84",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "Improved",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "-4% from yesterday",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF10B981)
                        )
                    }
                }

                // Smooth Spline Line Graph on the right using Native Canvas!
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .padding(start = 12.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val points = listOf(
                            Offset(0f, size.height * 0.9f),
                            Offset(size.width * 0.2f, size.height * 0.7f),
                            Offset(size.width * 0.4f, size.height * 0.8f),
                            Offset(size.width * 0.6f, size.height * 0.4f),
                            Offset(size.width * 0.8f, size.height * 0.5f),
                            Offset(size.width, size.height * 0.15f)
                        )
                        val path = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 1 until points.size) {
                                val prev = points[i - 1]
                                val curr = points[i]
                                val control1 = Offset(prev.x + (curr.x - prev.x) / 2, prev.y)
                                val control2 = Offset(prev.x + (curr.x - prev.x) / 2, curr.y)
                                cubicTo(control1.x, control1.y, control2.x, control2.y, curr.x, curr.y)
                            }
                        }
                        // Draw smooth blue stroke line
                        drawPath(
                            path = path,
                            color = ReclaimIndigo,
                            style = Stroke(width = 3.dp.toPx())
                        )
                        // Draw final dot
                        drawCircle(
                            color = ReclaimIndigo,
                            radius = 5.dp.toPx(),
                            center = points.last()
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Grid of Stats Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatGridCard(
                icon = Icons.Outlined.Devices,
                tint = ReclaimIndigo,
                title = "Screen Time",
                value = "3h 12m",
                modifier = Modifier.weight(1f)
            )
            StatGridCard(
                icon = Icons.Outlined.Timer,
                tint = Color(0xFFEF4444),
                title = "Focus Time",
                value = "${user?.totalFocusHours ?: 142f}h",
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatGridCard(
                icon = Icons.Default.LocalFireDepartment,
                tint = Color(0xFFF59E0B),
                title = "Streak",
                value = "${user?.streakDays ?: 12} days",
                modifier = Modifier.weight(1f)
            )
            StatGridCard(
                icon = Icons.Outlined.QueryStats,
                tint = Color(0xFF10B981),
                title = "Daily Goal",
                value = "${if (totalCount > 0) (completedCount * 100) / totalCount else 75}%",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Quick Sub-Features Triggers Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SPECIALIZED DISCIPLINES",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextGray
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fitness Card trigger
            Card(
                onClick = { viewModel.navigateTo("fitness_screen") },
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.DirectionsRun, contentDescription = "Fitness", tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Physical Vitality", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Steps & logging", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }

            // Meditation / Wellness trigger
            Card(
                onClick = { viewModel.navigateTo("wellness_screen") },
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.Spa, contentDescription = "Wellness", tint = ReclaimIndigo)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Mental Clarity", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Text("Reflections & breath", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Today's Progress Section header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "TODAY'S HABITS",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextGray
                )
                Text(
                    text = "$completedCount of $totalCount completed",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )
            }

            Text(
                text = "View All",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = ReclaimIndigo,
                modifier = Modifier.clickable { viewModel.selectTab("habits") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Habits List
        if (habits.isEmpty()) {
            Text(
                text = "No habits created yet. Tap the Habits tab to create your first tracking target!",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                modifier = Modifier.padding(vertical = 12.dp)
            )
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (h in habits) {
                    DashboardHabitItem(
                        habit = h,
                        onToggle = { viewModel.toggleHabitCompleted(h) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun StatGridCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BorderGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }
    }
}

@Composable
fun DashboardHabitItem(
    habit: HabitEntity,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("habit_item_${habit.id}"),
        colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = habit.completedToday,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = PrimaryDark,
                        checkmarkColor = Color.White,
                        uncheckedColor = TextGray
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (habit.completedToday) TextGray else TextDark,
                        textDecoration = if (habit.completedToday) TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${habit.schedule}  ·  ${habit.category}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            if (habit.streak > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Streak",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${habit.streak}d",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF59E0B)
                    )
                }
            }
        }
    }
}
