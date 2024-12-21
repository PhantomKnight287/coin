package com.phantomknight287.coin.db.app_state

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.phantomknight287.coin.screens.categories.Category
import com.phantomknight287.coin.screens.categories.CategoryDao

@Entity(tableName = "app_state")
data class AppState(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "categories_onboarding_completed")
    val categoriesOnboardingCompleted: Boolean = false,
)

@Dao
interface AppStateDao {
    @Upsert
    suspend fun insertAppState(state: AppState)

    @Delete
    suspend fun deleteAppState(state: AppState)

    @Query("Select * from app_state limit 1")
    suspend fun getAppState(): AppState?

    @Insert
    suspend fun insertCategories(categories: List<Category>)

    @Transaction
    suspend fun insertCategoriesAndAppState(categories: List<Category>, appState: AppState) {
        insertAppState(appState)
        insertCategories(categories)
    }
}