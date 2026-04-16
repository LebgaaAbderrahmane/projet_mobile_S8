package com.queuemanager.app.ui.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.queuemanager.app.ui.theme.*

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToChangePassword: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    var notificationsEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .verticalScroll(scrollState)
    ) {
        // Top Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                .background(LightTeal)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = SurfaceColor,
                        shadowElevation = 4.dp
                    ) {
                        Icon(
                            Icons.Default.Person, 
                            contentDescription = null, 
                            modifier = Modifier.padding(20.dp).size(50.dp),
                            tint = PrimaryTeal
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onNavigateToPersonalInfo() },
                        shape = CircleShape,
                        color = PrimaryTeal,
                        shadowElevation = 2.dp
                    ) {
                        Icon(
                            Icons.Default.Edit, 
                            contentDescription = null, 
                            tint = Color.White,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "${user?.firstName ?: "User"} ${user?.lastName ?: ""}",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkTeal
                    )
                )
                Text(
                    text = user?.email ?: "email@example.com",
                    style = MaterialTheme.typography.bodyMedium.copy(color = SlateGrey)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Account Settings
        SettingsSection(
            title = "Account Settings",
            items = listOf(
                SettingsItem("Personal Information", Icons.Default.Person, "Manage your name and contact") {
                    onNavigateToPersonalInfo()
                },
                SettingsItem("Change Password", Icons.Default.Lock, "Update your security") {
                    onNavigateToChangePassword()
                },
                SettingsItem("Past Reservations", Icons.Default.History, "View your history") {
                    Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // App Preferences
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "APP PREFERENCES",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = DarkTeal,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            PreferenceItem(
                title = "Language",
                icon = Icons.Default.Language,
                value = "English",
                onClick = { Toast.makeText(context, "Language selection coming soon!", Toast.LENGTH_SHORT).show() }
            )
            
            PreferenceItem(
                title = "Theme",
                icon = Icons.Default.Palette,
                value = "System Default",
                onClick = { Toast.makeText(context, "Theme selection coming soon!", Toast.LENGTH_SHORT).show() }
            )
            
            PreferenceToggle(
                title = "Notifications",
                icon = Icons.Default.Notifications,
                checked = notificationsEnabled,
                onCheckedChange = { 
                    notificationsEnabled = it
                    Toast.makeText(context, "Notification settings will be updated soon!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout Section
        AppButton(
            text = "Logout",
            onClick = { viewModel.logout(onLogout) },
            containerColor = Color(0xFFFEECEC),
            contentColor = Color(0xFFE53935),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SettingsSection(title: String, items: List<SettingsItem>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge.copy(
                color = DarkTeal,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        items.forEach { item ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { item.onClick() },
                shape = RoundedCornerShape(20.dp),
                color = SurfaceColor,
                shadowElevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(item.icon, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.title, fontWeight = FontWeight.Bold, color = DarkTeal)
                        Text(text = item.subtitle, style = MaterialTheme.typography.bodySmall.copy(color = SlateGrey))
                    }
                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SlateGrey)
                }
            }
        }
    }
}

@Composable
fun PreferenceItem(title: String, icon: ImageVector, value: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = SurfaceColor,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontWeight = FontWeight.Bold, color = DarkTeal, modifier = Modifier.weight(1f))
            Text(text = value, color = SlateGrey, style = MaterialTheme.typography.bodyMedium)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = DarkTeal)
        }
    }
}

@Composable
fun PreferenceToggle(title: String, icon: ImageVector, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        color = SurfaceColor,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryTeal, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, fontWeight = FontWeight.Bold, color = DarkTeal, modifier = Modifier.weight(1f))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = PrimaryTeal,
                    uncheckedThumbColor = SlateGrey,
                    uncheckedTrackColor = LightTeal
                )
            )
        }
    }
}

data class SettingsItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String,
    val onClick: () -> Unit
)
