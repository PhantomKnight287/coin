package com.phantomknight287.coin.screens.categories

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.TypeConverter
import java.io.Serializable


enum class CategoryType(val value: Int) {
    Expense(0),
    Income(1)
}

class CategoryTypeConverter {
    @TypeConverter
    fun fromCategoryType(value: CategoryType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toHealth(value: Int): CategoryType {
        return when (value) {
            0 -> CategoryType.Expense
            1 -> CategoryType.Income
            else -> CategoryType.Expense
        }
    }
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String,
    /**
     * The hex color for bg of icon
     */
    val color: String,
    val type: CategoryType,
    @ColumnInfo(name = "from_database")
    var fromDatabase: Boolean = false,
) : Serializable


@Dao
interface CategoryDao {
    @Query("Select * from categories")
    suspend fun getAll(): List<Category>

    @Query("Select * from categories where type=1")
    suspend fun getIncomeCategories(): List<Category>

    @Query("Select * from categories where type=0")
    suspend fun getExpenseCategories(): List<Category>

    @Insert
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)
}

val SUGGESTED_EXPENSE_CATEGORIES = mutableListOf(
    Category(
        name = "Food",
        icon = "🍔",
        color = "#FFA07A",
        type = CategoryType.Expense,
    ),  // Light Salmon
    Category(
        name = "Transport", icon = "🚗", color = "#ADD8E6",
        type = CategoryType.Expense,
    ),  // Light Blue
    Category(
        name = "Rent", icon = "🏠", color = "#FFFACD",
        type = CategoryType.Expense,
    ),  // Lemon Chiffon
    Category(
        name = "Shopping", icon = "🛒", color = "#FFB6C1",
        type = CategoryType.Expense,
    ),  // Light Pink
    Category(
        name = "Health", icon = "🩺", color = "#90EE90",
        type = CategoryType.Expense,
    ),  // Light Green
    Category(
        name = "Utilities", icon = "💡", color = "#DDA0DD",
        type = CategoryType.Expense,
    ),  // Plum
    Category(
        name = "Entertainment", icon = "🎬", color = "#FFDAB9",
        type = CategoryType.Expense,
    ),  // Peach Puff
    Category(
        name = "Education", icon = "📚", color = "#AFEEEE",
        type = CategoryType.Expense,
    ),  // Pale Turquoise
    Category(
        name = "Subscriptions", icon = "💳", color = "#F4A460",
        type = CategoryType.Expense,
    ),  // Sandy Brown
    Category(
        name = "Others", icon = "🔧", color = "#D3D3D3",
        type = CategoryType.Expense,
    )   // Light Grey
)

val SUGGESTED_INCOME_CATEGORIES = mutableListOf(
    Category(
        name = "Salary",
        icon = "💼",
        color = "#98FB98",
        type = CategoryType.Income
    ),  // Pale Green
    Category(
        name = "Investments",
        icon = "📈",
        color = "#B0E0E6",
        type = CategoryType.Income
    ),  // Powder Blue
    Category(
        name = "Business",
        icon = "🏢",
        color = "#FFFFE0",
        type = CategoryType.Income
    ),  // Light Yellow
    Category(
        name = "Freelance",
        icon = "🖥️",
        color = "#FFC0CB",
        type = CategoryType.Income
    ),  // Pink
    Category(
        name = "Gifts",
        icon = "🎁",
        color = "#FFD700",
        type = CategoryType.Income
    ),  // Light Gold
    Category(
        name = "Rental Income",
        icon = "🏘️",
        color = "#E6E6FA",
        type = CategoryType.Income
    ),  // Lavender
    Category(
        name = "Interest",
        icon = "🏦",
        color = "#FFF0F5",
        type = CategoryType.Income
    ),  // Lavender Blush
    Category(
        name = "Dividends",
        icon = "📊",
        color = "#FFE4B5",
        type = CategoryType.Income
    ),  // Moccasin
    Category(
        name = "Refunds",
        icon = "🔄",
        color = "#FFF5EE",
        type = CategoryType.Income
    ),  // Seashell
    Category(
        name = "Others",
        icon = "✨",
        color = "#F0FFF0",
        type = CategoryType.Income
    )   // Honeydew
)