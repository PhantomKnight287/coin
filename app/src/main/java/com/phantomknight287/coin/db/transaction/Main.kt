package com.phantomknight287.coin.db.transaction

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import androidx.room.TypeConverter
import com.phantomknight287.coin.screens.categories.Category
import java.time.Instant
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0,
    val amount: Double,
    val notes: String?,
    val createdAt: Date = Date.from(Instant.now()),
    val categoryId: Long,
)


data class TransactionWithCategory(
    @Embedded(prefix = "transaction_") val transaction: Transaction, // The transaction details
    @Embedded(prefix = "category_") val category: Category // The associated category
)

@Dao
interface TransactionDao {
    @Query(
        """Select  
            c.categoryId as category_categoryId,
c.type as category_type,
c.name as category_name,
c.color as category_color,
c.icon as category_icon,
t.categoryId as transaction_categoryId,
t.createdAt as transaction_createdAt,
t.amount as transaction_amount,
t.notes as transaction_notes,
t.transactionId as transaction_transactionId
from transactions t join categories c on c.categoryId = t.categoryId 
order by transaction_createdAt DESC 
"""
    )
    suspend fun getAllTransactionsWithCategories(): List<TransactionWithCategory>

    @Query(
        """
    SELECT transactions.* 
    FROM transactions 
    JOIN categories ON transactions.categoryId = categories.categoryId
    WHERE categories.type = 0
"""
    )
    suspend fun getAllExpenseTransactions(): List<Transaction>

    @Query(
        """
    SELECT transactions.* 
    FROM transactions 
    JOIN categories ON transactions.categoryId = categories.categoryId
    WHERE categories.type = 1
"""
    )
    suspend fun getAllIncomeTransactions(): List<Transaction>

    @Query(
        """
    SELECT 
        SUM(CASE 
                WHEN categories.type = 1 THEN transactions.amount
                WHEN categories.type = 0 THEN -transactions.amount
                ELSE 0
            END) AS balance
    FROM transactions
    JOIN categories ON transactions.categoryId = categories.categoryId
"""
    )
    suspend fun getBalance(): Double?

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

}


class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
