package com.phantomknight287.coin.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.phantomknight287.coin.db.app_state.AppState
import com.phantomknight287.coin.db.app_state.AppStateDao
import com.phantomknight287.coin.db.transaction.DateTypeConverter
import com.phantomknight287.coin.db.transaction.Transaction
import com.phantomknight287.coin.db.transaction.TransactionDao
import com.phantomknight287.coin.screens.categories.Category
import com.phantomknight287.coin.screens.categories.CategoryDao
import com.phantomknight287.coin.screens.categories.CategoryTypeConverter

@Database(entities = [Category::class, AppState::class, Transaction::class], version = 1)
@TypeConverters(CategoryTypeConverter::class, DateTypeConverter::class)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun appStateDao(): AppStateDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        // Volatile ensures the instance is immediately visible to other threads
        @Volatile
        private var INSTANCE: CoinDatabase? = null

        // Thread-safe method to get database instance
        fun getDatabase(context: Context): CoinDatabase {
            // If instance already exists, return it
            return INSTANCE ?: synchronized(this) {
                // Double-checked locking pattern
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create database instance
        private fun buildDatabase(context: Context): CoinDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CoinDatabase::class.java,
                "coin_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}