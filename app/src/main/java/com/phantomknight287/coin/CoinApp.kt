package com.phantomknight287.coin

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.phantomknight287.coin.db.CoinDatabase
import com.phantomknight287.coin.screens.categories.CategoriesScreen
import com.phantomknight287.coin.screens.categories.CategoriesViewModel
import com.phantomknight287.coin.screens.categories.Category
import com.phantomknight287.coin.screens.create_category.CreateCategoryViewModel
import com.phantomknight287.coin.screens.create_category.CreateNewCategoryScreen
import com.phantomknight287.coin.screens.welcome.WelcomeScreen
import com.google.gson.Gson
import com.phantomknight287.coin.screens.create_transaction.CreateTransactionScreen
import com.phantomknight287.coin.screens.create_transaction.CreateTransactionViewModel
import com.phantomknight287.coin.screens.home.FABClick
import com.phantomknight287.coin.screens.home.HomeScreen
import com.phantomknight287.coin.screens.home.HomeViewModel
import kotlinx.coroutines.launch

enum class Screens {
    Welcome,
    Categories,
    CreateCategory,
    Home,
    CreateTransaction,
}

@Composable
fun CoinApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val database = CoinDatabase.getDatabase(LocalContext.current)
    val scope = rememberCoroutineScope()
    val categoriesViewModel =
        CategoriesViewModel(database) // declaring it here to persist the flow changes across screens, otherwise selected categories get cleared when user comes back from create new category screen

    LaunchedEffect(true) {
        scope.launch {
            try {
                if (database.appStateDao().getAppState() != null) {
                    navController.navigate(
                        Screens.Home.name
                    ) {
                        popUpTo(Screens.Welcome.name) {
                            inclusive = true
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screens.Welcome.name,
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
        composable(route = Screens.Categories.name) { back ->
            CategoriesScreen(
                onNewButtonPress = {
                    if (it != null) {
                        navController.navigate(
                            "${Screens.CreateCategory.name}?category=${Uri.encode(Gson().toJson(it))}"
                        )
                    } else
                        navController.navigate(
                            Screens.CreateCategory.name
                        )
                },
                onContinue = {
                    navController.navigate(Screens.Home.name) {
                        popUpTo(Screens.Categories.name) {
                            inclusive = true
                        }
                    }
                },
                viewModel = categoriesViewModel,
            )
        }

        composable(route = "${Screens.CreateCategory.name}?category={category}", arguments = listOf(
            navArgument("category") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )) {
            val json = it.arguments?.getString("category")
            val category = json?.let {
                Gson().fromJson<Category>(it, Category::class.java)
            }
            CreateNewCategoryScreen(
                category = category,
                viewModel = CreateCategoryViewModel(database),
                onBack = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screens.Home.name,
        ) {
            HomeScreen(
                viewModel = HomeViewModel(database),
                onFABClick = {
                    if (it == FABClick.Transaction) {
                        navController.navigate(Screens.CreateTransaction.name)
                    }
                }
            )
        }

        composable(route = Screens.CreateTransaction.name) {
            val viewModel = CreateTransactionViewModel(database)
            CreateTransactionScreen(
                onBack = {
                    navController.navigateUp()
                },
                categoriesViewModel = categoriesViewModel,
                viewModel = viewModel,
            )
        }
    }
}