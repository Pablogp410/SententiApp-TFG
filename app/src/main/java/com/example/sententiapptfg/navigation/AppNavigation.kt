package com.example.sententiapptfg.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sententiapptfg.screens.article.ArticleScreen
import com.example.sententiapptfg.screens.home.HomeScreen
import com.example.sententiapptfg.screens.myquotes.MyQuotesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("myquotes") { MyQuotesScreen(navController) }
        composable(
            route = "articles/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            ArticleScreen(navController, id)
        }
    }
}