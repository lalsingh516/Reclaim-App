package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.outlined.*
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

@Composable
fun ProfileScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val user by viewModel.userState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header and notifications badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your Profile",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )

            // Achievements trigger icon button
            IconButton(
                onClick = { viewModel.navigateTo("achievements_screen") },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(ReclaimIndigoLight)
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Achievements",
                    tint = ReclaimIndigo
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Avatar Profile Badge Area
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(ReclaimIndigoLight)
                .border(BorderStroke(2.dp, ReclaimIndigo), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = ReclaimIndigo,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = user?.name ?: "Marcus Thorne",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark
        )
        Text(
            text = user?.email ?: "marcus.thorne@reclaim.io",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Streaks and Premium subscription badge
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .background(Color(0xFFFEF3C7), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${user?.streakDays ?: 12} Day Streak",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309)
                )
            }

            if (user?.isPremium == true) {
                Row(
                    modifier = Modifier
                        .background(ReclaimIndigoLight, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = null,
                        tint = ReclaimIndigo,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Premium Member",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = ReclaimIndigo
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Multi Stats summaries
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Focus Score", style = MaterialTheme.typography.bodySmall, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${user?.focusScore ?: 84}%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Total Focus", style = MaterialTheme.typography.bodySmall, color = TextGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${user?.totalFocusHours ?: 142f}h",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Intercept triggers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ReclaimIndigoLight)
                .clickable { viewModel.navigateTo("achievements_screen") }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = ReclaimIndigo,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Claim Redeemable Rewards",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDark
                    )
                    Text(
                        text = "You have ${user?.points ?: 2850} points available to redeem",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = ReclaimIndigo,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        val isFirebaseActive by com.example.data.FirebaseSyncManager.isFirebaseActive.collectAsState()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (isFirebaseActive) Color(0xFFECFDF5) else Color(0xFFF9FAFB)),
            border = BorderStroke(1.dp, if (isFirebaseActive) Color(0xFF10B981) else BorderGray),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isFirebaseActive) Icons.Default.CloudQueue else Icons.Default.CloudOff,
                    contentDescription = null,
                    tint = if (isFirebaseActive) Color(0xFF10B981) else TextGray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isFirebaseActive) "Firebase Sync Connected" else "Offline Local Sandbox",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isFirebaseActive) Color(0xFF065F46) else PrimaryDark
                    )
                    Text(
                        text = if (isFirebaseActive) "Your custom habits & logs sync to Firestore instantly." else "Running locally. Configure Firebase Secrets to unlock cloud backup.",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isFirebaseActive) Color(0xFF047857) else TextGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Settings items list
        Text(
            text = "SETTINGS & PARAMETERS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(12.dp))

        SettingsItemRow(
            icon = Icons.Outlined.CheckCircle,
            title = "Personal Goals",
            subtitle = "Active focus: ${user?.selectedFocusGoal}",
            onClick = { viewModel.navigateTo("onboarding_welcome") }
        )
        SettingsItemRow(
            icon = Icons.Outlined.Notifications,
            title = "Notification Settings",
            subtitle = "Calibrate smart screen alerts",
            onClick = { }
        )
        SettingsItemRow(
            icon = Icons.Outlined.ScreenLockPortrait,
            title = "App Blocking Control",
            subtitle = "Block reels and scrolling instantly",
            onClick = { viewModel.navigateTo("replace_reels") }
        )
        SettingsItemRow(
            icon = Icons.Outlined.Shield,
            title = "Privacy & Security",
            subtitle = "Manage local biometrics & profiles",
            onClick = { }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // System section & Log Out
        Button(
            onClick = { viewModel.logOut() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f))
        ) {
            Text(
                text = "Log Out Account",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // App details
        Text(
            text = "Reclaim Version 2.4.1 (Build 884)\nDesigned by humans to counter scrolling addiction.",
            style = MaterialTheme.typography.bodySmall,
            color = TextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun SettingsItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
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
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextDark,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
