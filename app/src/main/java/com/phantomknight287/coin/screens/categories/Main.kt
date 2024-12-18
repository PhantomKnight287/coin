package com.phantomknight287.coin.screens.categories

import android.os.VibrationEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.em
import com.phantomknight287.coin.constants.CoinConstants
import com.phantomknight287.coin.screens.categories.components.Switcher
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.phantomknight287.coin.helpers.getVibrator
import com.phantomknight287.coin.screens.categories.components.CreateCategoryModal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    onNewButtonPress: (Category?) -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    var selectedTab by rememberSaveable {
        mutableStateOf("Income")
    }
    val selectedIncomeCategories by viewModel.incomeCategories.collectAsState(initial = emptyList());
    val selectedExpenseCategories by viewModel.expenseCategories.collectAsState(initial = emptyList());
    val context = LocalContext.current
    val vib = getVibrator(context)
    val coroutineScope = rememberCoroutineScope()
    Scaffold(modifier,
        topBar = {
            TopAppBar(title = {
                Text(
                    "Categories",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), navigationIcon = { null },
                actions = {
                    IconButton(
                        onClick = {

                        },
                        enabled = selectedIncomeCategories.isNotEmpty() || selectedExpenseCategories.isNotEmpty()
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                            contentDescription = "Continue"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(
                        bottom = 16.dp,
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Switcher(
                    activeTab = selectedTab,
                    onTabChange = {
                        selectedTab = it
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onNewButtonPress(null)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New")
                    }
                }
            }
        }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                if (selectedTab == "Income") "Selected income categories" else
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
                if (
                    (if (selectedTab == "Income")
                        selectedIncomeCategories
                    else selectedExpenseCategories)
                        .isNotEmpty()
                ) Column {
                    (if (selectedTab == "Income") selectedIncomeCategories else selectedExpenseCategories).mapIndexed { index, item ->
                        Column {
                            CategoryItem(item, selected = true,
                                onClick = {
                                    if (item.fromDatabase) {
                                        onNewButtonPress(item)
                                    } else {
                                        if (item.type == CategoryType.Expense) {
                                            viewModel.removeExpenseCategory(item)
                                        } else {
                                            viewModel.removeIncomeCategory(item)
                                        }
                                    }
                                }
                            )
                            if (index != (if (selectedTab == "Income") selectedIncomeCategories else selectedExpenseCategories).lastIndex) {
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
                            "No categories found, click the 'New' button to add some or select one from suggested",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            if ((if (selectedTab == "Income") SUGGESTED_INCOME_CATEGORIES else SUGGESTED_EXPENSE_CATEGORIES).isNotEmpty()) {
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
                        (if (selectedTab == "Income") SUGGESTED_INCOME_CATEGORIES else SUGGESTED_EXPENSE_CATEGORIES).mapIndexed { index, item ->
                            Column {
                                CategoryItem(item, onClick = {
                                    if (selectedTab == "Income") {
                                        viewModel.addIncomeCategory(item)
                                        SUGGESTED_INCOME_CATEGORIES.removeAt(index)
                                    } else {
                                        viewModel.addExpenseCategory(item)
                                        SUGGESTED_EXPENSE_CATEGORIES.removeAt(index)
                                    }

                                    vib.vibrate(
                                        VibrationEffect.createOneShot(
                                            50L,
                                            VibrationEffect.DEFAULT_AMPLITUDE,
                                        )
                                    )
                                })
                                if (index != (if (selectedTab == "Income") SUGGESTED_INCOME_CATEGORIES else SUGGESTED_EXPENSE_CATEGORIES).lastIndex) {
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
            .padding(8.dp),
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
