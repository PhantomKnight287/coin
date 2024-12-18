package com.phantomknight287.coin.screens.categories.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.TagFaces
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.phantomknight287.coin.ui.theme.CoinTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCategoryModal(
    onDismissRequest: () -> Unit,
    onSave: (String) -> Unit,
    onColorPick: () -> Unit,
    onEmojiPick: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }


    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)) // Dark background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Emoji Picker Button (Center)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(16.dp))
                        .clickable { onEmojiPick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.TagFaces, // Emoji icon
                        contentDescription = "Emoji Picker",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Bottom Row: Color Picker, Input, Save Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Color Picker Button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFEF6C00), shape = RoundedCornerShape(8.dp))
                            .clickable { onColorPick() }
                    )

                    // Input Field
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

                    // Save Button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, shape = RoundedCornerShape(8.dp))
                            .clickable { onSave(inputText) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add, // Plus icon
                            contentDescription = "Save",
                            tint = Color.Black
                        )
                    }
                }

                // Auto-focus the input field
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        }
    }
}

@Preview
@Composable
private fun CreateCategoryModalPreview() {
    CoinTheme {
        CreateCategoryModal(
            onDismissRequest = {},
            onSave = {},
            onColorPick = {},
            onEmojiPick = {},
        )
    }
}