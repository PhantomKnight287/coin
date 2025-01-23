package com.phantomknight287.coin.screens.home.subscreens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.constants.CoinConstants

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        item {
            Text(
                text = "Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
            )
            Spacer(
                modifier = Modifier.height(16.dp),
            )
            Text(
                text = "Data",
                style = MaterialTheme.typography.labelSmall,
                color = CoinConstants.GreySubtle,
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            ListItem(
                headlineContent = {
                    Text("Categories")
                },
                leadingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.ViewList,
                        contentDescription = "Categories Icon"
                    )
                },

            )
        }

    }
}