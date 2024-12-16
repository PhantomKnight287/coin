package com.phantomknight287.coin.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phantomknight287.coin.ui.theme.CoinTheme
import com.phantomknight287.coin.R
import com.phantomknight287.coin.BuildConfig
import com.phantomknight287.coin.constants.CoinConstants

@Composable
fun WelcomeScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Application Icon",
                    modifier = Modifier
                        .width(70.dp)
                        .height(70.dp),
                )
                Text(
                    stringResource(R.string.app_name),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = "Version ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(22.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                FEATURES.map {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Icon(
                            it.icon, contentDescription = it.title,
                            modifier = Modifier.size(25.dp)
                        )
                        Column {
                            Text(
                                it.title, fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                it.description,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = CoinConstants.GreySubtle,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .padding(30.dp),
                contentPadding = PaddingValues(10.dp),
            ) {
                Text(
                    "Get Started",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun WelcomeScreenPreview() {
    CoinTheme {
        WelcomeScreen(onContinue = {})
    }
}