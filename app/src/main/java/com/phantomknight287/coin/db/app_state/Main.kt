package com.phantomknight287.coin.db.app_state

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Upsert

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
    suspend fun getAppState(): AppState
}