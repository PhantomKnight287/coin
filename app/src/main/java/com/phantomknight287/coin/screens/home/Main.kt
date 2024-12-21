package com.phantomknight287.coin.screens.home

import android.icu.util.Currency
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.R
import com.phantomknight287.coin.constants.CoinConstants
import com.phantomknight287.coin.db.transaction.Transaction
import com.phantomknight287.coin.screens.categories.Category
import com.phantomknight287.coin.screens.categories.CategoryType
import com.phantomknight287.coin.ui.theme.CoinTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

enum class FABClick {
    Transaction
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onFABClick: (FABClick) -> Unit,
) {
    val locale = Locale.getDefault()
    val currency = Currency.getInstance(locale)
    val balance by viewModel.balance.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getBalance()
        }
    }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getTransactions()
        }
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onFABClick(FABClick.Transaction)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            BottomAppBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.ReceiptLong,
                            contentDescription = "Transactions",

                            )
                    },
                    label = {
                        Text(
                            "Transactions",
                            fontSize = 14.sp,
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            painterResource(R.drawable.outline_monitoring_24),
                            contentDescription = "Insights",
                        )
                    },
                    label = {
                        Text(
                            "Analytics",
                            fontSize = 14.sp,
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Filled.Inventory2,
                            contentDescription = "Budgets",
                        )
                    },
                    label = {
                        Text(
                            "Budgets",
                            fontSize = 14.sp,
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings",
                        )
                    },
                    label = {
                        Text(
                            "Settings",
                            fontSize = 14.sp,
                        )
                    },
                )
            }
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),

            ) {
            item() {
                Row {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search transactions"
                    )
                }
            }
            item() {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
            item() {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Net total")
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "${if (balance < 0) "-" else ""}${currency.symbol}",
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 24.sp
                            )
                            Text(
                                text = balance.absoluteValue.toString(),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier.height(32.dp),
                    )

                }
            }
            itemsIndexed(
                transactions
            ) { index, item ->
                if (index == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = SimpleDateFormat(
                                "E, dd MMM",
                                Locale.getDefault()
                            ).format(item.transaction.createdAt),
                            fontWeight = FontWeight.Bold,
                        )
                        val dayTotal = transactions
                            .filter { it.transaction.createdAt.day == item.transaction.createdAt.day }
                            .sumOf { it.transaction.amount }
                        Text(
                            text = formatCurrency(dayTotal, currency),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                } else if (item.transaction.createdAt.day != transactions[index - 1].transaction.createdAt.day
                ) {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = SimpleDateFormat(
                                "E, dd MMM",
                                Locale.getDefault()
                            ).format(item.transaction.createdAt),
                            fontWeight = FontWeight.Bold,
                        )
                        val dayTotal = transactions
                            .filter { it.transaction.createdAt.day == item.transaction.createdAt.day }
                            .sumOf { it.transaction.amount }
                        Text(
                            text = formatCurrency(dayTotal, currency),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp
                    )
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
                TransactionItem(
                    category = item.category,
                    transaction = item.transaction,
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}


@Composable
fun TransactionItem(
    category: Category,
    transaction: Transaction,
    modifier: Modifier = Modifier
) {
    val locale = Locale.getDefault()
    val currency = Currency.getInstance(locale)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(
                    Color(android.graphics.Color.parseColor(category.color)),
                )
                .padding(4.dp),
        ) {
            Text(
                text = category.icon,
                fontSize = 22.sp,
            )
        }
        Column {
            Text(
                text = transaction.notes?.takeUnless { it.isEmpty() } ?: category.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = SimpleDateFormat("hh:mm", locale).format(transaction.createdAt),
                fontWeight = FontWeight.Light,
                fontSize = 12.sp,
                color = CoinConstants.GreySubtle,
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        when (category.type) {
            CategoryType.Income -> Text(
                text = "+${currency.symbol}${transaction.amount}",
                fontWeight = FontWeight.SemiBold,
            )

            CategoryType.Expense -> Text(
                text = "-${currency.symbol}${transaction.amount}",
                fontWeight = FontWeight.SemiBold,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionItemPreview() {
    CoinTheme {
        TransactionItem(
            transaction = Transaction(
                amount = 500.0,
                notes = "Test Notes",
                categoryId = 1,
                createdAt = Date.from(Instant.now()),
            ),
            category = Category(
                name = "Tax",
                icon = "\uD83D\uDC80",
                color = colorToHex(Color.Red.toArgb()),
                type = CategoryType.Expense,
            )
        )
    }

}

private fun colorToHex(color: Int): String {
    return String.format("#%06X", 0xFFFFFF and color)
}

fun formatCurrency(amount: Double, currency: Currency): String {
    return "${if (amount < 0) "-" else ""}${currency.symbol}${amount.absoluteValue}"
}