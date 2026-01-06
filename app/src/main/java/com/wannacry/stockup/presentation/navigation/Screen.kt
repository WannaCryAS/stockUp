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
    object TaskDetail : Screen("task_detail_screen/{taskId}") {
        fun createRoute(taskId: String) = "task_detail_screen/$taskId"
    }
}