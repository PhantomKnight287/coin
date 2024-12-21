package com.phantomknight287.coin.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phantomknight287.coin.db.CoinDatabase
import com.phantomknight287.coin.db.transaction.TransactionWithCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val database: CoinDatabase) : ViewModel() {
    private var _transactions = MutableStateFlow<List<TransactionWithCategory>>(listOf())
    val transactions = _transactions.asStateFlow()
    private var _balance = MutableStateFlow(0.0)
    val balance = _balance.asStateFlow()

    suspend fun getBalance() {
        try {
            val _queriedBalance = database.transactionDao().getBalance()
            _balance.value =
                if (_queriedBalance == null) (0.toDouble()) else _queriedBalance.toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getTransactions() {
        try {
            _transactions.value = database.transactionDao().getAllTransactionsWithCategories()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        viewModelScope.launch {
            getBalance()
            getTransactions()
        }
    }
}