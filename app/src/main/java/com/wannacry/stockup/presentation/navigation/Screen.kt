package com.wannacry.stockup.presentation.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home_screen")
    object AddItem : Screen("add_item_screen")
    object EditItem : Screen("edit_item_screen/{itemId}") {
        fun createRoute(itemId: String) = "edit_item_screen/$itemId"
    }
    object ItemDetail : Screen("item_detail_screen/{itemId}") {
        fun createRoute(itemId: String) = "item_detail_screen/$itemId"
    }
}

//sealed class BottomNavItem(
//    val route: String,
//    val title: String,
//    val icon: ImageVector
//) {
//    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
//    object Reports : BottomNavItem("reports", "Laporan", Icons.Default.ListAlt)
//    object Task : BottomNavItem("task", "Task", Icons.Default.Task)
//    object Settings : BottomNavItem("settings", "Tetapan", Icons.Default.Settings)
//}