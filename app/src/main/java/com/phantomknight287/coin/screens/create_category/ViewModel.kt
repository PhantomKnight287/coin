package com.phantomknight287.coin.screens.create_category

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomknight287.coin.db.CoinDatabase
import com.phantomknight287.coin.screens.categories.Category
import kotlinx.coroutines.launch

class CreateCategoryViewModel(private val database: CoinDatabase) : ViewModel() {
    suspend fun createCategory(category: Category) {
        database.categoryDao().insert(category)
    }

    suspend fun deleteCategory(category: Category) {
        database.categoryDao().delete(category)
    }
}