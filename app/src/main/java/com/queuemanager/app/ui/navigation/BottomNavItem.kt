package com.queuemanager.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Queue
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    // Customer Tabs
    object Home : BottomNavItem("home", "Discover", Icons.Default.Search)
    object Reservations : BottomNavItem("reservations", "My Queues", Icons.Default.History)
    
    // Owner Tabs (Shared Dashboard vs specific Management)
    object OwnerDashboard : BottomNavItem("owner_dashboard", "Home", Icons.Default.Dashboard)
    object ManageQueues : BottomNavItem("manage_queues", "Queues", Icons.Default.Queue)
    
    // Shared
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}
