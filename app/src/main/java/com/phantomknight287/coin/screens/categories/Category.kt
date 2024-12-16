package com.phantomknight287.coin.screens.categories

data class Category(
    val name: String,
    val icon: String,
    /**
     * The hex color for bg of icon
     */
    val color: String
)

val SUGGESTED_EXPENSE_CATEGORIES = mutableListOf(
    Category(name = "Food", icon = "🍔", color = "#FFA07A"),  // Light Salmon
    Category(name = "Transport", icon = "🚗", color = "#ADD8E6"),  // Light Blue
    Category(name = "Rent", icon = "🏠", color = "#FFFACD"),  // Lemon Chiffon
    Category(name = "Shopping", icon = "🛒", color = "#FFB6C1"),  // Light Pink
    Category(name = "Health", icon = "🩺", color = "#90EE90"),  // Light Green
    Category(name = "Utilities", icon = "💡", color = "#DDA0DD"),  // Plum
    Category(name = "Entertainment", icon = "🎬", color = "#FFDAB9"),  // Peach Puff
    Category(name = "Education", icon = "📚", color = "#AFEEEE"),  // Pale Turquoise
    Category(name = "Subscriptions", icon = "💳", color = "#F4A460"),  // Sandy Brown
    Category(name = "Others", icon = "🔧", color = "#D3D3D3")   // Light Grey
)

val SUGGESTED_INCOME_CATEGORIES = mutableListOf(
    Category(name = "Salary", icon = "💼", color = "#98FB98"),  // Pale Green
    Category(name = "Investments", icon = "📈", color = "#B0E0E6"),  // Powder Blue
    Category(name = "Business", icon = "🏢", color = "#FFFFE0"),  // Light Yellow
    Category(name = "Freelance", icon = "🖥️", color = "#FFC0CB"),  // Pink
    Category(name = "Gifts", icon = "🎁", color = "#FFD700"),  // Light Gold
    Category(name = "Rental Income", icon = "🏘️", color = "#E6E6FA"),  // Lavender
    Category(name = "Interest", icon = "🏦", color = "#FFF0F5"),  // Lavender Blush
    Category(name = "Dividends", icon = "📊", color = "#FFE4B5"),  // Moccasin
    Category(name = "Refunds", icon = "🔄", color = "#FFF5EE"),  // Seashell
    Category(name = "Others", icon = "✨", color = "#F0FFF0")   // Honeydew
)