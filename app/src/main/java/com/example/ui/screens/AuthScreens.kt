package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.ReclaimViewModel

@Composable
fun AuthWelcomeScreen(viewModel: ReclaimViewModel) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Header Icon
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = "Reclaim Icon",
            modifier = Modifier.size(48.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Reclaim",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryDark
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Large Premium Illustration placeholder (laptop workspace mockup standard)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.LaptopMac,
                        contentDescription = "Laptop",
                        modifier = Modifier.size(64.dp),
                        tint = TextGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your digital sanctuary",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Title and Subtitle
        Text(
            text = "Take back your time.",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Reclaim is your digital sanctuary. Designed to help you build healthier digital habits, minimize distractions, and rediscover focus in an always-on world.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Button(
            onClick = { viewModel.navigateTo("auth_signup") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ReclaimIndigo)
        ) {
            Text(
                text = "Get Started",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { viewModel.navigateTo("auth_login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, BorderGray)
        ) {
            Text(
                text = "Sign In",
                color = PrimaryDark,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bullets Checkmarks List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BulletItem(text = "Intelligent distraction blocking")
            BulletItem(text = "Curated focus environments")
            BulletItem(text = "Real-time digital wellness coaching")
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Feature cards
        Text(
            text = "DESIGNED FOR INTENTIONAL LIVING",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextGray,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        FeatureTeaserCard(
            icon = Icons.Default.Timer,
            title = "Intentional Usage",
            desc = "Set conscious limits on apps that drain your energy and focus."
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeatureTeaserCard(
            icon = Icons.Default.TrendingUp,
            title = "Deep Analytics",
            desc = "Understand your digital patterns with detailed, non-judgmental reports."
        )
        Spacer(modifier = Modifier.height(16.dp))
        FeatureTeaserCard(
            icon = Icons.Default.Spa,
            title = "Focus Modes",
            desc = "One-tap environments tailored for work, rest, or creativity."
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Footer Copyright
        Text(
            text = "© 2026 Reclaim Digital Wellness.\nPrivacy  ·  Terms  ·  Contact",
            style = MaterialTheme.typography.bodySmall,
            color = TextGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BulletItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = "Check",
            modifier = Modifier.size(20.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = TextDark
        )
    }
}

@Composable
fun FeatureTeaserCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ReclaimSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(ReclaimIndigoLight, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = ReclaimIndigo
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
fun LoginScreen(viewModel: ReclaimViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authError by viewModel.authError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = "Brand Logo",
            modifier = Modifier.size(40.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sign in to resume reclaiming your time.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (authError != null) {
            Text(
                text = authError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Email field
        Text(
            text = "EMAIL ADDRESS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("you@example.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Mail, contentDescription = "Email") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Password field
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PASSWORD",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "Forgot password?",
                style = MaterialTheme.typography.labelSmall,
                color = ReclaimIndigo,
                modifier = Modifier.clickable { viewModel.navigateTo("auth_reset") }
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.login(email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
        ) {
            Text("Sign In", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Federated sign-in alternatives
        Text(
            text = "OR CONTINUE WITH",
            style = MaterialTheme.typography.labelSmall,
            color = TextGray,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.login("demo@reclaim.io", "password123") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Icon(Icons.Default.Android, contentDescription = "Google Sign In", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Google", color = PrimaryDark)
            }
            OutlinedButton(
                onClick = { viewModel.login("demo@reclaim.io", "password123") },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Icon(Icons.Default.Fingerprint, contentDescription = "Apple Sign In", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apple", color = PrimaryDark)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ", color = TextGray)
            Text(
                text = "Sign up",
                color = ReclaimIndigo,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo("auth_signup") }
            )
        }
    }
}

@Composable
fun SignUpScreen(viewModel: ReclaimViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authError by viewModel.authError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = "Brand Logo",
            modifier = Modifier.size(40.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Create your account",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start building dynamic digital discipline.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authError != null) {
            Text(
                text = authError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        // Full Name
        Text(
            text = "YOUR NAME",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Marcus Thorne") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "Name") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email address
        Text(
            text = "EMAIL ADDRESS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("you@example.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Mail, contentDescription = "Email") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Personal password
        Text(
            text = "PASSWORD (MIN. 6 CHARACTERS)",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "Password") }
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.signUp(name, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
        ) {
            Text("Create Account", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Already have an account? ", color = TextGray)
            Text(
                text = "Sign in",
                color = ReclaimIndigo,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { viewModel.navigateTo("auth_login") }
            )
        }
    }
}

@Composable
fun ResetScreen(viewModel: ReclaimViewModel) {
    var email by remember { mutableStateOf("") }
    var success by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = "Brand Logo",
            modifier = Modifier.size(40.dp),
            tint = ReclaimIndigo
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Reset password",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your email to receive recovery instructions.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (success) {
            Card(
                colors = CardDefaults.cardColors(containerColor = ReclaimIndigoLight),
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, ReclaimIndigo)
            ) {
                Text(
                    text = "Success! Recovery link sent details to $email. Please check your spam folder.",
                    style = MaterialTheme.typography.bodySmall,
                    color = ReclaimIndigo,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Email address
        Text(
            text = "EMAIL ADDRESS",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("you@example.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ReclaimIndigo,
                unfocusedBorderColor = BorderGray
            ),
            singleLine = true,
            leadingIcon = { Icon(Icons.Outlined.Mail, contentDescription = "Email") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.resetPassword(email) { res ->
                    success = res
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryDark)
        ) {
            Text("Send Instructions", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Back to Sign In",
            color = TextGray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { viewModel.navigateTo("auth_login") }
        )
    }
}
