package com.wannacry.stockup.domain.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wannacry.stockup.config.converter.InstantConverter
import com.wannacry.stockup.config.converter.LocalDateConverter
import com.wannacry.stockup.config.converter.UUIDConverter
import com.wannacry.stockup.domain.dao.CategoryDao
import com.wannacry.stockup.domain.dao.ItemDao
import com.wannacry.stockup.domain.dao.StockHistoryDao
import com.wannacry.stockup.domain.dao.TaskDao
import com.wannacry.stockup.domain.dao.TaskItemDao
import com.wannacry.stockup.domain.db.entity.CategoryEntity
import com.wannacry.stockup.domain.db.entity.ItemEntity
import com.wannacry.stockup.domain.db.entity.StockHistoryEntity
import com.wannacry.stockup.domain.db.entity.TaskEntity
import com.wannacry.stockup.domain.db.entity.TaskItemEntity

@Database(
    entities = [
        CategoryEntity::class, 
        ItemEntity::class, 
        StockHistoryEntity::class, 
        TaskEntity::class,
        TaskItemEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(UUIDConverter::class, LocalDateConverter::class, InstantConverter::class)
abstract class StockUpDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun itemDao(): ItemDao
    abstract fun stockHistoryDao(): StockHistoryDao
    abstract fun taskDao(): TaskDao
    abstract fun taskItemDao(): TaskItemDao

    companion object {
        @Volatile private var INSTANCE: StockUpDatabase? = null

        fun getDatabase(context: Context): StockUpDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    StockUpDatabase::class.java,
                    "stockUp_db"
                ).fallbackToDestructiveMigration() // Handle changes by wiping old data
                .build().also { INSTANCE = it }
            }
        }
    }
}
