package com.phantomknight287.coin

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.phantomknight287.coin.screens.categories.CategoriesScreen
import com.phantomknight287.coin.screens.welcome.WelcomeScreen

enum class Screens {
    Welcome,
    Categories,
}

@Composable
fun CoinApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screens.Categories.name,
        modifier,
    ) {
        composable(
            route = Screens.Welcome.name
        ) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(Screens.Categories.name) {
                        popUpTo(Screens.Welcome.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = Screens.Categories.name) {
            CategoriesScreen(

            )
        }

    }
}