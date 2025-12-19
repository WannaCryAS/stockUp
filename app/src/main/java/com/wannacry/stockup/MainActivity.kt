package com.wannacry.stockup

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wannacry.stockup.presentation.navigation.BottomNavItem
import com.wannacry.stockup.presentation.navigation.Screen
import com.wannacry.stockup.presentation.screen.AddItemScreen
import com.wannacry.stockup.presentation.screen.HomeScreen
import com.wannacry.stockup.presentation.screen.ItemDetailScreen
import com.wannacry.stockup.presentation.screen.SettingsScreen
import com.wannacry.stockup.ui.theme.StockUpTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockUpTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val bottomNavItems = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Task,
                    BottomNavItem.Reports,
                    BottomNavItem.Settings
                )

                val shouldShowBottomBar = bottomNavItems.any { it.route == currentDestination?.route }

                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(28.dp),
                                    color = MaterialTheme.colorScheme.surfaceContainer,
                                    tonalElevation = 8.dp,
                                    shadowElevation = 12.dp
                                ) {
                                    NavigationBar(
                                        containerColor = Color.Transparent,
                                        modifier = Modifier.height(80.dp),
                                        tonalElevation = 0.dp,
                                        windowInsets = WindowInsets(0, 0, 0, 0)
                                    ) {
                                        bottomNavItems.forEach { screen ->
                                            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                                            NavigationBarItem(
                                                icon = { if (selected) Icon(screen.selectedIcon, contentDescription = screen.title) else Icon(screen.icon, contentDescription = screen.title) },
                                                label = { Text(screen.title) },
                                                selected = selected,
                                                alwaysShowLabel = selected,
                                                onClick = {
                                                    navController.navigate(screen.route) {
                                                        popUpTo(navController.graph.findStartDestination().id) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController,
                        startDestination = BottomNavItem.Home.route,
                        modifier = Modifier.padding(PaddingValues(
                            start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                            end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                            bottom = innerPadding.calculateBottomPadding()
                        ))
                    ) {
                        composable(BottomNavItem.Home.route) { HomeScreen(navController) }
                        composable(BottomNavItem.Task.route) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Task Screen") }
                        }
                        composable(BottomNavItem.Reports.route) { 
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Reports Screen") }
                        }
                        composable(BottomNavItem.Settings.route) { SettingsScreen(navController) }
                        composable(Screen.AddItem.route) { AddItemScreen(navController) }
                        composable(
                            route = Screen.EditItem.route,
                            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId")
                            if (itemId != null) {
                                AddItemScreen(navController = navController, itemId = itemId)
                            }
                        }
                        composable(
                            route = Screen.ItemDetail.route,
                            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val itemId = backStackEntry.arguments?.getString("itemId")
                            if (itemId != null) {
                                ItemDetailScreen(navController = navController, itemId = itemId)
                            }
                        }
                    }
                }
            }
        }
    }
}