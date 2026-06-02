package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.coroutines.launch

@Composable
fun AchievementsScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val user by viewModel.userState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var redemptionMessage by remember { mutableStateOf<String?>(null) }

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
                text = "Achievements & Rewards",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (redemptionMessage != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = ReclaimIndigoLight),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, ReclaimIndigo)
            ) {
                Text(
                    text = redemptionMessage ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = ReclaimIndigo,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Auto-clear message after 3 seconds
            LaunchedEffect(redemptionMessage) {
                delay(3000)
                redemptionMessage = null
            }
        }

        // Points Circle Progress Tracker Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DISCIPLINE POINTS BALANCE",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${user?.points ?: 2850}",
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "pts",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Level ${user?.level ?: 12}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = ReclaimIndigo
                    )
                    Text(
                        text = "150 points until Level ${((user?.level ?: 12) + 1)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Milestones progress line
                LinearProgressIndicator(
                    progress = { 0.85f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape),
                    color = ReclaimIndigo,
                    trackColor = BorderGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Earned Badges section
        Text(
            text = "EARNED DISCIPLINE BADGES",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BadgeItem(
                icon = Icons.Default.LocalFireDepartment,
                color = Color(0xFFF59E0B),
                title = "7-Day Streak",
                subtitle = "Consistency"
            )
            BadgeItem(
                icon = Icons.Default.Timer,
                color = ReclaimIndigo,
                title = "Focus Master",
                subtitle = "Deep work"
            )
            BadgeItem(
                icon = Icons.Default.AutoAwesome,
                color = Color(0xFF10B981),
                title = "Early Adopter",
                subtitle = "Pioneer"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Redeemable rewards section
        Text(
            text = "REDEEM DISCIPLINE REWARDS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray
        )
        Spacer(modifier = Modifier.height(12.dp))

        RewardRedeemTile(
            title = "Premium Month Credit",
            description = "Get 30 days of unlimited deep analytics & custom environments.",
            cost = 1200,
            hasPoints = (user?.points ?: 2850) >= 1200,
            onRedeem = {
                val success = viewModel.redeemReward(1200)
                redemptionMessage = if (success) {
                    "Successfully redeemed Premium Month Credit! 1,200 points deducted."
                } else {
                    "Insufficient points balance."
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        RewardRedeemTile(
            title = "Digital Minimalism E-book",
            description = "Sleek copy of 'The Art of Boredom' by Cal Newport guides.",
            cost = 800,
            hasPoints = (user?.points ?: 2850) >= 800,
            onRedeem = {
                val success = viewModel.redeemReward(800)
                redemptionMessage = if (success) {
                    "Successfully redeemed Digital Minimalism electronic book copy! 800 points deducted."
                } else {
                    "Insufficient points balance."
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun BadgeItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f))
                .border(2.dp, color, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = TextGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RewardRedeemTile(
    title: String,
    description: String,
    cost: Int,
    hasPoints: Boolean,
    onRedeem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GeneratingTokens,
                        contentDescription = "Token cost",
                        tint = ReclaimIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$cost Points",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ReclaimIndigo
                    )
                }
            }

            Button(
                onClick = onRedeem,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasPoints) PrimaryDark else BorderGray,
                    contentColor = if (hasPoints) Color.White else TextGray
                ),
                enabled = hasPoints,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Redeem")
            }
        }
    }
}
