package com.example.sententiapptfg.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sententiapptfg.data.SententiAppRepository
import com.example.sententiapptfg.data.SoapService
import com.example.sententiapptfg.screens.article.ArticleScreen
import com.example.sententiapptfg.screens.article.ArticleViewModel
import com.example.sententiapptfg.screens.home.HomeScreen
import com.example.sententiapptfg.screens.home.HomeViewModel
import com.example.sententiapptfg.screens.myquotes.MyQuotesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        //composable("home") { HomeScreen(navController) }
        composable("home") {
            val repository = SententiAppRepository(SoapService())
            val viewModel = remember {
                HomeViewModel(repository)
            }
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("myquotes") { MyQuotesScreen(navController) }
        composable(
            route = "articles/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val repository = SententiAppRepository(SoapService())
            val viewModel = remember {
                ArticleViewModel(repository)
            }
            ArticleScreen(
                navController= navController,
                viewModel=viewModel,
                dateId = id)
        }
    }
}