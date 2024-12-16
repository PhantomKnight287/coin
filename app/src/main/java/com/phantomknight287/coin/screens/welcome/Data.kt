package com.phantomknight287.coin.screens.welcome

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.ui.graphics.vector.ImageVector

data class Feature(
    val title: String,
    val description: String,
    val icon: ImageVector,
)

val FEATURES = listOf<Feature>(
    Feature(
        title = "Track your finances",
        description = "Add new entries in a single, straightforward manner",
        icon = Icons.Filled.Equalizer,
    ),
    Feature(
        title = "Analyse your expenditure",
        description = "Draw insights from your spending over various time periods",
        icon = Icons.Filled.Analytics,
    ),
    Feature(
        title = "Stick to budgets",
        description = "Set categorical and time-based goals to limit your spending",
        icon = Icons.Filled.Inventory2,
    )
)