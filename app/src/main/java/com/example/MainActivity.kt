package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel
import com.example.ui.viewmodel.ReclaimViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Setup custom Lazy Database provider Factory
        val app = application as ReclaimApp
        val factory = ReclaimViewModelFactory(app, app.repository)
        val viewModel = ViewModelProvider(this, factory)[ReclaimViewModel::class.java]

        setContent {
            MyApplicationTheme {
                ReclaimAppLayout(viewModel)
            }
        }
    }
}

@Composable
fun ReclaimAppLayout(viewModel: ReclaimViewModel) {
    val route by viewModel.currentRoute.collectAsState()
    val activeTab by viewModel.selectedTab.collectAsState()

    when (route) {
        // --- Authentication Routing ---
        "auth_welcome" -> AuthWelcomeScreen(viewModel)
        "auth_login" -> LoginScreen(viewModel)
        "auth_signup" -> SignUpScreen(viewModel)
        "auth_reset" -> ResetScreen(viewModel)

        // --- Onboarding Routing ---
        "onboarding_welcome" -> OnboardingWelcomeScreen(viewModel)
        "onboarding_goal" -> OnboardingGoalScreen(viewModel)
        "onboarding_phone" -> OnboardingPhoneScreen(viewModel)
        "onboarding_details" -> OnboardingDetailsScreen(viewModel)
        "onboarding_personalizing" -> OnboardingPersonalizingScreen(viewModel)

        // --- Extranets / Focus Interceptors ---
        "replace_reels" -> BlockerScreen(viewModel)
        "paywall_screen" -> PaywallScreen(viewModel)
        "achievements_screen" -> AchievementsScreen(viewModel)
        "wellness_screen" -> WellnessScreen(viewModel)
        "fitness_screen" -> FitnessScreen(viewModel)
        "admin_panel" -> AdminPanelScreen(viewModel)

        // --- Main Tabbed Application Hub ---
        "main_dashboard" -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 8.dp,
                        modifier = Modifier.border(BorderStroke(1.dp, BorderGray))
                    ) {
                        val navigationTabs = listOf(
                            Triple("dashboard", "Dashboard", Icons.Outlined.Dashboard),
                            Triple("habits", "Habits", Icons.Outlined.CheckCircle),
                            Triple("focus", "Focus", Icons.Outlined.Timer),
                            Triple("coach", "Coach", Icons.Outlined.SmartToy),
                            Triple("profile", "Profile", Icons.Outlined.Person)
                        )

                        for (tab in navigationTabs) {
                            val isSelected = activeTab == tab.first
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.selectTab(tab.first) },
                                icon = {
                                    Icon(
                                        imageVector = tab.third,
                                        contentDescription = tab.second,
                                        tint = if (isSelected) ReclaimIndigo else TextGray
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.second,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) ReclaimIndigo else TextGray,
                                        fontSize = 11.sp
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = ReclaimIndigoLight
                                )
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    when (activeTab) {
                        "dashboard" -> DashboardScreen(viewModel)
                        "habits" -> HabitsScreen(viewModel)
                        "focus" -> FocusScreen(viewModel)
                        "coach" -> CoachScreen(viewModel)
                        "profile" -> ProfileScreen(viewModel)
                    }
                }
            }
        }
    }
}
