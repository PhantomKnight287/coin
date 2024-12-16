package com.phantomknight287.coin.screens.categories

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.ui.theme.CoinTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.em
import com.phantomknight287.coin.constants.CoinConstants
import com.phantomknight287.coin.screens.categories.components.Switcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = CategoriesViewModel(), modifier: Modifier = Modifier
) {
    var scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val selectedIncomeCategories by viewModel.incomeCategories.collectAsState(initial = emptyList());
    val selectedExpenseCategories by viewModel.expenseCategories.collectAsState(initial = emptyList());
    val context = LocalContext.current
    val vib = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    Scaffold(modifier,
        topBar = {
            TopAppBar(title = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = 16.dp,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "Categories",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), navigationIcon = { null })
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                    )
                    .padding(horizontal = 4.dp)
            ) {
                Switcher(
                    onTabChange = {}
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {}) {
                    Text("+ New")
                }
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(4.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Selected expense categories",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
            ) {
                if (selectedExpenseCategories.isNotEmpty()) Column {
                    selectedExpenseCategories.mapIndexed { index, item ->
                        Column {
                            CategoryItem(item, selected = true)
                            if (index != selectedExpenseCategories.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                else {
                    Column(
                        modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            Icons.Filled.Inbox,
                            contentDescription = "Categories Icon",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            "No expense categories found, click the 'New' button to add some or select one from suggested",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            if (SUGGESTED_EXPENSE_CATEGORIES.isNotEmpty()) {

                Text(
                    "Suggested categories",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
                ) {
                    Column {
                        SUGGESTED_EXPENSE_CATEGORIES.mapIndexed { index, item ->
                            Column {
                                CategoryItem(item, onClick = {
                                    viewModel.addExpenseCategory(item)
                                    SUGGESTED_EXPENSE_CATEGORIES.removeAt(index)
                                    vib.vibrate(
                                        VibrationEffect.createOneShot(
                                            50L,
                                            VibrationEffect.DEFAULT_AMPLITUDE,
                                        )
                                    )
                                })
                                if (index != SUGGESTED_EXPENSE_CATEGORIES.lastIndex) {
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.onPrimaryContainer,
                                            )
                                    )
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit = {},
    selected: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            category.icon,
            fontSize = 15.sp,
        )
        Text(
            category.name,
            fontSize = 18.5.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Box(modifier = Modifier
                .background(
                    color = Color(android.graphics.Color.parseColor(category.color)),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
                .clickable {
                    onClick()
                }) {}
        } else {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .padding(0.dp)
                    .size(24.dp),
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Select ${category.name}",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryPreview() {
    CoinTheme { CategoriesScreen() }
}