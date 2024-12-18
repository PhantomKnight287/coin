package com.phantomknight287.coin.screens.create_category

import android.graphics.Color.parseColor
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.emoji2.emojipicker.EmojiPickerView
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.phantomknight287.coin.screens.categories.Category
import com.phantomknight287.coin.screens.categories.CategoryType
import com.phantomknight287.coin.screens.categories.components.Switcher
import com.phantomknight287.coin.ui.theme.CoinTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewCategoryScreen(
    viewModel: CreateCategoryViewModel,
    category: Category? = null,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    var selectedTab by rememberSaveable {
        mutableIntStateOf(category?.type?.ordinal ?: CategoryType.Income.ordinal)
    }
    var inputText by remember {
        mutableStateOf(
            category?.name ?: ""
        )
    }
    val focusRequester = remember { FocusRequester() }
    val controller = rememberColorPickerController()
    var color by rememberSaveable {
        mutableStateOf(category?.color ?: "#ef6c00")
    }
    var colorPickerActive by rememberSaveable {
        mutableStateOf(false)
    }
    var emojiPickerActive by rememberSaveable { mutableStateOf(false) }
    var emoji by rememberSaveable {
        mutableStateOf(category?.icon ?: "\uD83C\uDFDB\uFE0F")
    }
    var loading by rememberSaveable {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "Create new category",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }, scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), navigationIcon = {
                IconButton(
                    onClick = onBack,
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
            },
                actions = {
                    if (category != null)
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        viewModel.deleteCategory(category)
                                        onBack()
                                    } catch (e: Exception) {

                                    }
                                }
                            },
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .background(Color.Red.copy(alpha = 0.25f))

                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                contentDescription = "Delete Category"
                            )
                        }
                }
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Switcher(
                activeTab = if (selectedTab == 0) "Expense" else "Income",
                onTabChange = {
                    when (it) {
                        "Income" -> selectedTab = 1
                        "Expense" -> selectedTab = 0
                    }
                },
            )
            when (colorPickerActive) {
                true -> AlertDialog(
                    onDismissRequest = {
                        colorPickerActive = false
                    },
                    title = {
                        Text(text = "Pick a Color")
                    },
                    text = {
                        Column {
                            Card {
                                HsvColorPicker(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(450.dp)
                                        .padding(10.dp),
                                    controller = controller,
                                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                                        color = "#${colorEnvelope.hexCode.removePrefix("ff")}"
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(8.dp)
                                    .background(
                                        Color(parseColor(color)),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                colorPickerActive = false
                            }
                        ) {
                            Text(text = "Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            colorPickerActive = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                )

                false -> null
            }
            when (emojiPickerActive) {
                true -> Dialog(
                    onDismissRequest = {
                        emojiPickerActive = false
                    }
                ) {
                    Card {
                        AndroidView(
                            factory = {
                                EmojiPickerView(it).apply {
                                    setOnEmojiPickedListener {
                                        emoji = it.emoji
                                        emojiPickerActive = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                false -> null
            }

            Spacer(modifier = Modifier.height(40.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
                    .clickable {
                        emojiPickerActive = true
                    }
            ) {
                Text(
                    text = emoji,
                    fontSize = 36.sp,
                )
            }


            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(parseColor(color)), shape = RoundedCornerShape(8.dp))
                        .clickable {
                            colorPickerActive = !colorPickerActive
                        }
                )

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    placeholder = { Text("Category Name", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,

                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            if (!loading) {
                                loading = true
                                coroutineScope.launch {
                                    try {
                                        viewModel.createCategory(
                                            Category(
                                                type = if (selectedTab == 0) CategoryType.Expense else CategoryType.Income,
                                                icon = emoji,
                                                name = inputText,
                                                color = color,
                                            )
                                        )
                                        onBack()
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    when (loading) {
                        true -> CircularProgressIndicator()
                        false -> Icon(
                            imageVector = Icons.Default.Add, // Plus icon
                            contentDescription = "Save",
                            tint = Color.Black
                        )
                    }

                }
            }

            // Auto-focus the input field
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

        }
    }
}
