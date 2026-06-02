package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel
import kotlinx.coroutines.delay

@Composable
fun OnboardingWelcomeScreen(viewModel: ReclaimViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Welcome to Reclaim",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Let's calibrate your focus profile in 5 quick steps.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { viewModel.startOnboarding() },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
        ) {
            Text("Start Calibrating", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

@Composable
fun OnboardingGoalScreen(viewModel: ReclaimViewModel) {
    var selectedGoal by remember { mutableStateOf("Reduce Screen Time") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Step indicator header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP 02 OF 05",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { viewModel.navigateTo("auth_welcome") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { 0.4f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = ReclaimIndigo,
            trackColor = BorderGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Heading
        Text(
            text = "What's your focus?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Select your primary goal to help us personalize your discipline journey.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Grid Options
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            GoalOptionCard(
                icon = Icons.Default.HighlightOff,
                title = "Reduce Screen Time",
                desc = "Reclaim hours lost to mindless scrolling and digital noise.",
                isSelected = selectedGoal == "Reduce Screen Time",
                onClick = { selectedGoal = "Reduce Screen Time" }
            )

            GoalOptionCard(
                icon = Icons.Default.CenterFocusStrong,
                title = "Improve Focus",
                desc = "Enter deep work states faster and maintain them longer.",
                isSelected = selectedGoal == "Improve Focus",
                onClick = { selectedGoal = "Improve Focus" }
            )

            GoalOptionCard(
                icon = Icons.Default.NightsStay,
                title = "Better Sleep",
                desc = "Optimize your evening wind-down and morning alertness.",
                isSelected = selectedGoal == "Better Sleep",
                onClick = { selectedGoal = "Better Sleep" }
            )

            GoalOptionCard(
                icon = Icons.Default.Psychology,
                title = "Mental Clarity",
                desc = "Reduce stress and anxiety through structured discipline.",
                isSelected = selectedGoal == "Mental Clarity",
                onClick = { selectedGoal = "Mental Clarity" }
            )
        }

        // Action steps
        Button(
            onClick = { viewModel.saveOnboardingGoal(selectedGoal) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
        ) {
            Text("Continue", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Skip for now",
            style = MaterialTheme.typography.labelMedium,
            color = TextGray,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { viewModel.saveOnboardingGoal("Reduce Screen Time") }
        )
    }
}

@Composable
fun GoalOptionCard(
    isActive: Boolean = false, // wait, isSelected
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ReclaimIndigoLight else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) ReclaimIndigo else BorderGray
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) ReclaimIndigo else TextGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun OnboardingPhoneScreen(viewModel: ReclaimViewModel) {
    var checkCount by remember { mutableStateOf(45f) }

    val feedbackText = remember(checkCount) {
        val count = checkCount.toInt()
        when {
            count < 25 -> "$count checks is moderate low. Excellent baseline! We'll keep things fine-tuned."
            count in 25..75 -> "$count checks is moderate. We'll help you reduce this by 20% in the first week."
            else -> "$count checks represents high reliance. We'll set steady milestones to rebuild your focus step-by-step!"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Step indicator header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP 03 OF 05",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Assessment", style = MaterialTheme.typography.labelSmall, color = ReclaimIndigo)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { viewModel.navigateTo("auth_welcome") }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { 0.6f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = ReclaimIndigo,
            trackColor = BorderGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Heading
        Text(
            text = "How many times do you check your phone?",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be honest with yourself. This data helps us tailor your focus sessions to your current habits.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Giant number centered
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${checkCount.toInt()}",
                    style = MaterialTheme.typography.displayLarge,
                    fontSize = 64.sp,
                    color = PrimaryDark,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "times / day",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextGray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Slider
            Slider(
                value = checkCount,
                onValueChange = { checkCount = it },
                valueRange = 0f..150f,
                colors = SliderDefaults.colors(
                    activeTrackColor = ReclaimIndigo,
                    thumbColor = ReclaimIndigo,
                    inactiveTrackColor = BorderGray
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("0", style = MaterialTheme.typography.bodySmall, color = TextGray)
                Text("50", style = MaterialTheme.typography.bodySmall, color = TextGray)
                Text("100", style = MaterialTheme.typography.bodySmall, color = TextGray)
                Text("150+", style = MaterialTheme.typography.bodySmall, color = TextGray)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Info Notice bulb card
            Card(
                colors = CardDefaults.cardColors(containerColor = ReclaimIndigoLight),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, ReclaimIndigo.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = "Bulb",
                        tint = ReclaimIndigo,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = feedbackText,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDark,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        // Bottom check action button array
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.navigateTo("onboarding_goal") },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Text("Back", color = PrimaryDark, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { viewModel.saveOnboardingChecks(checkCount.toInt()) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
            ) {
                Text("Next", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun OnboardingDetailsScreen(viewModel: ReclaimViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "STEP 04 OF 05",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { viewModel.navigateTo("auth_welcome") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { 0.8f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = ReclaimIndigo,
            trackColor = BorderGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Tell us about yourself",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We will calibrate your digital profile and local stats logs with these details.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(40.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "YOUR DISCIPLINE PROFILE NAME",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("e.g. Marcus Thorne") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ReclaimIndigo,
                    unfocusedBorderColor = BorderGray
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "CONTACT OR REFLECTION EMAIL",
                style = MaterialTheme.typography.labelSmall,
                color = TextDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("e.g. marcus@example.com") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ReclaimIndigo,
                    unfocusedBorderColor = BorderGray
                ),
                singleLine = true
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.navigateTo("onboarding_phone") },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Text("Back", color = PrimaryDark, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { viewModel.saveOnboardingDetails(name, email) },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
            ) {
                Text("Next", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun OnboardingPersonalizingScreen(viewModel: ReclaimViewModel) {
    // Center loader with animated visual simulation
    var personalizationFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2500)
        personalizationFinished = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!personalizationFinished) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(BorderStroke(1.dp, BorderGray), RoundedCornerShape(16.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = ReclaimIndigo
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Reclaim",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator(
                color = ReclaimIndigo,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Personalizing your digital sanctuary...",
                style = MaterialTheme.typography.bodySmall,
                color = TextGray
            )
        } else {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = ReclaimIndigo
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Calibration complete!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryDark
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your digital detox strategies are configured and ready.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { viewModel.completeOnboarding() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
            ) {
                Text("Enter Dashboard", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            }
        }
    }
}
