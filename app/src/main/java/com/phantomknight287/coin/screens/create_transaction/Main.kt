package com.phantomknight287.coin.screens.create_transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.phantomknight287.coin.db.transaction.Transaction
import com.phantomknight287.coin.screens.categories.CategoriesViewModel
import com.phantomknight287.coin.screens.categories.Category
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.util.Calendar
import java.util.Currency
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionScreen(
    viewModel: CreateTransactionViewModel,
    categoriesViewModel: CategoriesViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val currency = Currency.getInstance(Locale.getDefault())
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val currentTime = LocalTime.now()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute
    )
    val selectedDate =
        convertMillisToDate((datePickerState.selectedDateMillis ?: Instant.now().toEpochMilli()))
    var amount by rememberSaveable {
        mutableStateOf("")
    }
    // too lazy to implement custom saver to replace it with `rememberSaveable`
    var selectedCategory by remember {
        mutableStateOf<Category?>(null)
    }
    val incomeCategories = categoriesViewModel.incomeCategories.collectAsState()
    val expenseCategories = categoriesViewModel.expenseCategories.collectAsState()
    val categories = incomeCategories.value + expenseCategories.value

    var showCategoriesDropdown by rememberSaveable { mutableStateOf(false) }
    var notes by rememberSaveable {
        mutableStateOf("")
    }
    val coroutineScope = rememberCoroutineScope()
    var loading by rememberSaveable {
        mutableStateOf(false)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { null },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (selectedCategory == null) {
                                Toast.makeText(
                                    context,
                                    "Please select a category",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@IconButton
                            }
                            if (amount == "0") {
                                Toast.makeText(
                                    context,
                                    "Please enter an amount",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@IconButton
                            }
                            loading = true
                            coroutineScope.launch {
                                try {
                                    viewModel.createTransaction(
                                        Transaction(
                                            amount = amount.toDouble(),
                                            categoryId = selectedCategory!!.categoryId,
                                            notes = notes,
                                            createdAt = Date.from(
                                                datePickerState.selectedDateMillis?.let { dateMillis ->
                                                    val calendar = Calendar.getInstance().apply {
                                                        timeInMillis = dateMillis
                                                        set(
                                                            Calendar.HOUR_OF_DAY,
                                                            timePickerState.hour
                                                        )
                                                        set(Calendar.MINUTE, timePickerState.minute)
                                                        set(Calendar.SECOND, 0)
                                                        set(Calendar.MILLISECOND, 0)
                                                    }
                                                    Instant.ofEpochMilli(calendar.timeInMillis)
                                                } ?: Instant.now()
                                            )
                                        )
                                    )
                                    onBack()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT)
                                        .show()
                                } finally {
                                    loading = false
                                }
                            }
                        },
                        enabled = !loading
                    ) {
                        when (loading) {
                            true -> CircularProgressIndicator(

                            )

                            false -> Icon(
                                Icons.Filled.CheckCircle, contentDescription = "Save",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }

                    }
                }
            )
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(
                    horizontal = 16.dp,
                )
                .padding(bottom = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = currency.symbol,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 20.sp
                    )
                    if (amount.isNotEmpty()) {
                        Text(
                            text = amount,
                            fontSize = 26.sp
                        )
                    } else {
                        Text(
                            text = "0",
                            fontSize = 26.sp
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = {
                        notes = it
                    },
                    placeholder = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Notes,
                                contentDescription = "Notes"
                            )
                            Text("Note")
                        }
                    },
                    maxLines = 3,
                    shape = RoundedCornerShape(8.dp), // Rounded corners
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary
                    )
                )
            }
            Spacer(
                modifier = Modifier.weight(1f)
            )
            if (showDatePicker) {
                Dialog(
                    onDismissRequest = {
                        showDatePicker = false
                        // Don't show time picker if date picker is dismissed
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Column {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = true,
                                title = {
                                    null
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showDatePicker = false
                                        // Don't show time picker if cancelled
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        showDatePicker = false
                                        showTimePicker =
                                            true // Show time picker when Next is clicked
                                    }
                                ) {
                                    Text("Next")
                                }
                            }
                        }
                    }
                }
            }

            if (showTimePicker) {
                Dialog(
                    onDismissRequest = {
                        showTimePicker = false
                    },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp)
                    ) {
                        Column {
                            TimePicker(
                                state = timePickerState
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        showTimePicker = false
                                        showDatePicker = true // Go back to date picker
                                    }
                                ) {
                                    Text("Back")
                                }
                                TextButton(
                                    onClick = {
                                        showTimePicker = false
                                    }
                                ) {
                                    Text("OK")
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(
                        onClick = {
                            showDatePicker = true
                        },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)

                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                Icons.Filled.CalendarMonth,
                                contentDescription = "Date",
                                modifier = Modifier.size(16.dp)
                            )
                            Text(selectedDate)
                        }
                    }
                    TextButton(
                        onClick = {
                            showCategoriesDropdown = true
                        },
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .weight(1f)

                    ) {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                if (selectedCategory != null) {
                                    Text(selectedCategory!!.icon)
                                    Text(selectedCategory!!.name)
                                } else {
                                    Text(
                                        "Select Category",
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showCategoriesDropdown,
                                onDismissRequest = { showCategoriesDropdown = false }
                            ) {
                                categories.map {
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(it.icon)
                                                Text(it.name)
                                            }
                                        },
                                        onClick = {
                                            selectedCategory = it
                                            showCategoriesDropdown = false
                                        }
                                    )
                                }

                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KeyboardButton(
                        label = LabelContent.TextLabel("1"),
                        onClick = {
                            amount += "1"
                        },
                        modifier = Modifier.weight(1f)
                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("2"),
                        onClick = {
                            amount += "2"

                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("3"),
                        onClick = {
                            amount += "3"
                        },
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KeyboardButton(
                        label = LabelContent.TextLabel("4"),
                        onClick = {
                            amount += "4"
                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("5"),
                        onClick = {
                            amount += "5"
                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("6"),
                        onClick = {
                            amount += "6"
                        },
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KeyboardButton(
                        label = LabelContent.TextLabel("7"),
                        onClick = {
                            amount += "7"

                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("8"),
                        onClick = {
                            amount += "8"

                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("9"),
                        onClick = {
                            amount += "9"

                        },
                        modifier = Modifier.weight(1f)

                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    KeyboardButton(
                        label = LabelContent.TextLabel("."),
                        onClick = {
                            amount += "."
                        },
                        modifier = Modifier.weight(1f)
                    )
                    KeyboardButton(
                        label = LabelContent.TextLabel("0"),
                        onClick = {
                            amount += "0"

                        },
                        modifier = Modifier.weight(1f)

                    )
                    KeyboardButton(
                        label = LabelContent.ComposableLabel {
                            Icon(
                                Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                            )
                        },
                        onClick = {
                            amount = amount.dropLast(1)
                        },
                        disabled = amount.isEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.errorContainer)

                    )
                }
            }
        }
    }
}

sealed class LabelContent {
    data class TextLabel(val text: String) : LabelContent()
    data class ComposableLabel(val composable: @Composable () -> Unit) : LabelContent()
}

@Composable
fun KeyboardButton(
    label: LabelContent,
    onClick: () -> Unit,
    disabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .then(modifier),
        onClick = onClick,
        enabled = !disabled
    ) {
        when (label) {
            is LabelContent.TextLabel -> Text(
                label.text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 26.sp,
            )

            is LabelContent.ComposableLabel -> label.composable()
        }

    }
}

fun convertMillisToDate(millis: Long): String {
    val now = Calendar.getInstance()
    val dateToConvert = Calendar.getInstance().apply { timeInMillis = millis }

    val formatterToday = SimpleDateFormat("dd MMM", Locale.getDefault())
    val formatterFarAway = SimpleDateFormat("E, dd MMM yyyy", Locale.getDefault())

    return when {
        isSameDay(now, dateToConvert) -> "Today, ${formatterToday.format(dateToConvert.time)}"
        isWithinRange(now, dateToConvert, 7) -> formatterFarAway.format(dateToConvert.time)
        else -> formatterFarAway.format(dateToConvert.time)
    }
}

private fun isSameDay(calendar1: Calendar, calendar2: Calendar): Boolean {
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}

private fun isWithinRange(now: Calendar, date: Calendar, rangeDays: Int): Boolean {
    val diff = date.timeInMillis - now.timeInMillis
    val dayDifference = diff / (1000 * 60 * 60 * 24)
    return dayDifference in 0 until rangeDays
}
