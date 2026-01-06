package com.wannacry.stockup.config

enum class StockIndicator { AVAILABLE, LOW, EMPTY, EXPIRED }

object ActionType {
    const val ADD = "ADD"
    const val UPDATE = "UPDATE"
    const val REMOVE = "REMOVE"
}