package com.phantomknight287.coin.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Transaction
import com.phantomknight287.coin.db.CoinDatabase
import com.phantomknight287.coin.db.app_state.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(private val database: CoinDatabase) : ViewModel() {
    private val _selectedExpenseCategories = MutableStateFlow<List<Category>>(listOf())
    private val _selectedIncomeCategories = MutableStateFlow<List<Category>>(listOf())
    val expenseCategories: StateFlow<List<Category>> = _selectedExpenseCategories.asStateFlow()
    val incomeCategories: StateFlow<List<Category>> = _selectedIncomeCategories.asStateFlow();

    suspend fun fetchFromDB() {
        try {
            val items = database.categoryDao().getIncomeCategories()
            _selectedIncomeCategories.value = items.map { item ->
                item.fromDatabase = true
                item
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val items = database.categoryDao().getExpenseCategories()
            _selectedExpenseCategories.value = items.map {
                it.fromDatabase = true
                it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        viewModelScope.launch {
            fetchFromDB()
        }
    }


    suspend fun insertAppStateAndCategoriesTransaction(state: AppState) {
        val categoriesToInsert =
            (_selectedExpenseCategories.value + _selectedIncomeCategories.value)
                .filter { it.fromDatabase == false } // remove all categories already present in database
        database.appStateDao().insertCategoriesAndAppState(categoriesToInsert, state)
        fetchFromDB()  // this is to replace the existing `id=0` with actual IDs
    }

    suspend fun insertAllCategoriesToDatabase() {
        val categoriesToInsert =
            (_selectedExpenseCategories.value + _selectedIncomeCategories.value)
                .filter { it.fromDatabase == false } // remove all categories already present in database

        database.categoryDao().insertAll(categoriesToInsert)
    }

    fun addExpenseCategory(category: Category) {
        _selectedExpenseCategories.value = _selectedExpenseCategories.value + category
    }


    fun removeExpenseCategory(category: Category) {
        _selectedExpenseCategories.value =
            _selectedExpenseCategories.value.filter { it != category }
    }

    fun addIncomeCategory(category: Category) {
        _selectedIncomeCategories.value = _selectedIncomeCategories.value + category
    }

    fun removeIncomeCategory(category: Category) {
        _selectedIncomeCategories.value = _selectedIncomeCategories.value.filter { it != category }
    }
}