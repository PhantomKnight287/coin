package com.phantomknight287.coin.screens.categories.components

import android.os.VibrationEffect
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.ui.theme.CoinTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.phantomknight287.coin.helpers.getVibrator

@Composable
fun Switcher(
    activeTab: String,
    onTabChange: (tab: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("Income", "Expense")
    require(activeTab in tabs) { "Invalid activeTab value: $activeTab. Must be one of $tabs." }

    var selectedTab by rememberSaveable { mutableStateOf(activeTab) }

    val haptic = getVibrator(LocalContext.current)

    val animatedOffset by animateDpAsState(
        targetValue = if (selectedTab == "Income") 0.dp else 100.dp, label = "PillOffset",
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessMedium,
        )
    )

    val tabWidth = 100.dp
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(4.dp)
            .height(40.dp)
            .width(tabWidth * 2) // Total width for 2 tabs
    ) {
        // Sliding pill shape
        Box(
            modifier = Modifier
                .offset(x = animatedOffset) // Slide horizontally
                .size(tabWidth, 40.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
        )

        // Row for tabs
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            tabs.forEach { tab ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(tabWidth, 40.dp)
                        .clickable(
                            indication = null,
                            enabled = true,
                            interactionSource = interactionSource,
                        ) {
                            if (selectedTab != tab) {
                                selectedTab = tab
                                haptic.vibrate(
                                    VibrationEffect.createOneShot(
                                        50L,
                                        VibrationEffect.DEFAULT_AMPLITUDE,
                                    )
                                )
                                onTabChange(tab)
                            }
                        }
                ) {
                    Text(
                        text = tab,
                        color = if (tab == selectedTab) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SwitcherPreview() {
    CoinTheme {
        Switcher(
            activeTab = "Income",
            onTabChange = {}
        )
    }
}