package com.phantomknight287.coin.screens.create_transaction

import androidx.lifecycle.ViewModel
import com.phantomknight287.coin.db.CoinDatabase
import com.phantomknight287.coin.db.transaction.Transaction

class CreateTransactionViewModel(
    private val database: CoinDatabase
) : ViewModel() {
    suspend fun createTransaction(transaction: Transaction) {
        database.transactionDao().insertTransaction(transaction)
    }
}