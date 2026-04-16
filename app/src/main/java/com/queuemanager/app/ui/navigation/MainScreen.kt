package com.queuemanager.app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.queuemanager.app.ui.theme.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.ui.customer.HomeScreen
import com.queuemanager.app.ui.owner.OwnerDashboardScreen
import com.queuemanager.app.ui.profile.ProfileScreen

@Composable
fun MainScreen(
    userRole: UserRole,
    onLogout: () -> Unit,
    onNavigateToPersonalInfo: () -> Unit,
    onNavigateToChangePassword: () -> Unit
) {
    val navController = rememberNavController()
    
    val items = if (userRole == UserRole.CUSTOMER) {
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Reservations,
            BottomNavItem.Profile
        )
    } else {
        listOf(
            BottomNavItem.OwnerDashboard,
            BottomNavItem.ManageQueues,
            BottomNavItem.Profile
        )
    }

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(80.dp),
                shape = RoundedCornerShape(40.dp),
                color = SurfaceColor,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    
                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(if (selected) PrimaryTeal.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    item.icon, 
                                    contentDescription = item.label,
                                    tint = if (selected) PrimaryTeal else SlateGrey,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = item.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) PrimaryTeal else SlateGrey
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = items.first().route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(onBusinessClick = { /* TODO: Navigate to detail */ })
            }
            composable(BottomNavItem.Reservations.route) {
                // TODO: My Reservations Screen
                Surface { Text("My Reservations Screen (Coming Soon)") }
            }
            composable(BottomNavItem.OwnerDashboard.route) {
                OwnerDashboardScreen()
            }
            composable(BottomNavItem.ManageQueues.route) {
                // TODO: Owner Queue Management Screen
                Surface { Text("Queue Management Screen (Coming Soon)") }
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    onLogout = onLogout,
                    onNavigateToPersonalInfo = onNavigateToPersonalInfo,
                    onNavigateToChangePassword = onNavigateToChangePassword
                )
            }
        }
    }
}
