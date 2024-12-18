package com.phantomknight287.coin.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomknight287.coin.db.CoinDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel(private val database: CoinDatabase) : ViewModel() {
    private val _selectedExpenseCategories = MutableStateFlow<List<Category>>(listOf())
    private val _selectedIncomeCategories = MutableStateFlow<List<Category>>(listOf())
    val expenseCategories: StateFlow<List<Category>> = _selectedExpenseCategories.asStateFlow()
    val incomeCategories: StateFlow<List<Category>> = _selectedIncomeCategories.asStateFlow();

    init {
        viewModelScope.launch {
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