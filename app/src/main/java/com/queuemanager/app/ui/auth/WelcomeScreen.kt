package com.queuemanager.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.queuemanager.app.ui.theme.*

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(PrimaryTeal, RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color.White, modifier = Modifier.size(30.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Queue Manager",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            )
        }

        Surface(
            modifier = Modifier
                .size(280.dp),
            shape = RoundedCornerShape(40.dp),
            color = SurfaceColor,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureIcon(Modifier.weight(1f), Icons.Default.PersonAdd, Color(0xFF80EEFF))
                    FeatureIcon(Modifier.weight(1f), Icons.Default.HourglassEmpty, Color(0xFFE0E0E0))
                }
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureIcon(Modifier.weight(1f), Icons.Default.ElectricBolt, Color(0xFFFFDAB9))
                    FeatureIcon(Modifier.weight(1f), Icons.Default.LocationOn, Color(0xFFBDF3E7))
                }
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Skip the line,",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkTeal
                )
            )
            Text(
                text = "join from anywhere",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTeal
                )
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                text = "Sign Up",
                onClick = onSignUpClick,
                showArrow = true
            )
            AppButton(
                text = "Log In",
                onClick = onLoginClick,
                containerColor = Color.White,
                contentColor = DarkTeal
            )
        }
    }
}

@Composable
fun FeatureIcon(modifier: Modifier, icon: ImageVector, color: Color) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .background(color, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = DarkTeal, modifier = Modifier.size(32.dp))
    }
}
