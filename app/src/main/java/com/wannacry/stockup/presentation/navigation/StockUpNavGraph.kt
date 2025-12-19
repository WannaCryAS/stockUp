package com.wannacry.stockup.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wannacry.stockup.presentation.screen.AddItemScreen
import com.wannacry.stockup.presentation.screen.HomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StockUpNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.AddItem.route) {
            AddItemScreen(navController = navController)
        }
        // Add other composables for ItemDetail here later
    }
}
