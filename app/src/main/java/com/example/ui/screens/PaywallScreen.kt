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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel

@Composable
fun PaywallScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()
    val user by viewModel.userState.collectAsState()
    var billingCycle by remember { mutableStateOf("Yearly") } // "Monthly" vs "Yearly"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Reclaim Pro",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Large illustration representation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ReclaimIndigoLight),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = ReclaimIndigo,
                    modifier = Modifier.size(52.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Unlock your ultimate discipline",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = ReclaimIndigo
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Headings
        Text(
            text = "Master your digital focus",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Unlock advanced tools backed by behavioral science to permanently break scrolling habits.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Subscription benefit checklist rows
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            PremiumBenefitTile(
                icon = Icons.Default.Timer,
                title = "Unlimited Focus Modes",
                desc = "Customizable focus intervals and tasks lists for work, reading, or study environments."
            )
            PremiumBenefitTile(
                icon = Icons.Default.QueryStats,
                title = "Advanced Analytics",
                desc = "Breakdowns of daily/weekly pickups, trendlines, comparative graphs, and screen times."
            )
            PremiumBenefitTile(
                icon = Icons.Default.SmartToy,
                title = "Personalized AI Coach",
                desc = "Powered by Gemini to learn your schedule, suggest detours, and text wellness goals."
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Billing Selector Trigger Switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(ReclaimSurface)
                .border(1.dp, BorderGray, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            listOf("Monthly", "Yearly").forEach { b ->
                val active = billingCycle == b
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (active) Color.White else Color.Transparent)
                        .clickable { billingCycle = b }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = b,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (active) PrimaryDark else TextGray
                        )
                        if (b == "Yearly") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFEF3C7), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "SAVE 35%",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Plan Price description
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (billingCycle == "Monthly") "$12.00 / month" else "$7.50 / month",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDark
                )
                Text(
                    text = if (billingCycle == "Monthly") "Billed monthly. Cancel anytime." else "Billed annually ($89.99). Risk-free.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Subscription CTA trigger
        val isActivated = user?.isPremium == true
        Button(
            onClick = {
                viewModel.togglePremium()
                viewModel.navigateTo("main_dashboard")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isActivated) Color(0xFF10B981) else ReclaimIndigo
            )
        ) {
            Text(
                text = if (isActivated) "Cancel Active Plan" else "Start 7-Day Free Trial",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No commitments. Cancel from your profile in one tap. Trusted by 50k+ high achievers.",
            style = MaterialTheme.typography.bodySmall,
            color = TextGray,
            textAlign = TextAlign.Center,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
fun PremiumBenefitTile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ReclaimIndigoLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ReclaimIndigo,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = TextGray,
                lineHeight = 18.sp
            )
        }
    }
}
