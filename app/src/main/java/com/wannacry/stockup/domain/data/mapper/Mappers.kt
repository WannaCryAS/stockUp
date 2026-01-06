package com.wannacry.stockup.domain.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.domain.data.Category
import com.wannacry.stockup.domain.data.Item
import com.wannacry.stockup.domain.data.StockHistory
import com.wannacry.stockup.domain.data.Task
import com.wannacry.stockup.domain.db.entity.CategoryEntity
import com.wannacry.stockup.domain.db.entity.ItemEntity
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import com.wannacry.stockup.domain.db.entity.TaskEntity

@RequiresApi(Build.VERSION_CODES.O)
fun CategoryEntity.toDomain(): Category = Category(
    id = this.id,
    name = this.name,
    createdAt = this.createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = this.id,
    name = this.name,
    createdAt = this.createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun ItemEntity.toDomain(): Item = Item(
    id = this.id,
    name = this.name,
    description = this.description,
    quantity = this.quantity,
    unit = this.unit,
    expiryDate = this.expiryDate,
    categoryId = this.categoryId,
    stockIndicator = this.stockIndicator,
    minLimit = this.minLimit,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun Item.toEntity(): ItemEntity = ItemEntity(
    id = this.id,
    name = this.name,
    description = this.description,
    quantity = this.quantity,
    unit = this.unit,
    expiryDate = this.expiryDate,
    categoryId = this.categoryId,
    stockIndicator = this.stockIndicator,
    minLimit = this.minLimit,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun StockHistoryEntity.toDomain(): StockHistory = StockHistory(
    id = this.id,
    itemId = this.itemId,
    actionType = this.actionType,
    description = this.description,
    quantityChange = this.quantityChange,
    createdAt = this.createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun StockHistory.toEntity(): StockHistoryEntity = StockHistoryEntity(
    id = this.id,
    itemId = this.itemId,
    actionType = this.actionType,
    description = this.description,
    quantityChange = this.quantityChange,
    createdAt = this.createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun TaskEntity.toDomain(): Task = Task(
    id = this.id,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt
)

@RequiresApi(Build.VERSION_CODES.O)
fun Task.toEntity(): TaskEntity = TaskEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    createdAt = this.createdAt
)
