package com.wannacry.stockup.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : BottomNavItem("home", "Home", Icons.Outlined.Home, Icons.Default.Home)
    object Task : BottomNavItem("task", "Task", Icons.Outlined.Task, Icons.Default.Task)
    object Reports : BottomNavItem("reports", "Report", Icons.Outlined.Assessment, Icons.Default.Assessment)
    object Settings : BottomNavItem("settings", "Tetapan", Icons.Outlined.Settings, Icons.Default.Settings)
}