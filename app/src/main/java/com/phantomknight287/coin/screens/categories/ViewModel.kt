package com.phantomknight287.coin.screens.categories

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoriesViewModel : ViewModel() {
    private val _selectedExpenseCategories = MutableStateFlow<List<Category>>(listOf())
    private val _selectedIncomeCategories = MutableStateFlow<List<Category>>(listOf())
    val expenseCategories: StateFlow<List<Category>> = _selectedExpenseCategories.asStateFlow()
    val incomeCategories: StateFlow<List<Category>> = _selectedIncomeCategories.asStateFlow();

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