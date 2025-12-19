package com.example.sententiapptfg.screens.home

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sententiapptfg.data.FakeSententiAppRepository
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun homeScreen_loadsDatesAndCategories_fromFakeRepository() {
        // Arrange
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = HomeViewModel(fakeRepository)

        // Act
        composeRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Wait to load data
        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.filteredDates.value.isNotEmpty()
        }

        // Assert: checks the data is loaded correctly
        composeRule.onNodeWithText("getDates Tag 1").assertExists()
        composeRule.onNodeWithText("getDates Tag 2").assertExists()
        composeRule.onNodeWithText("getDates Tag 3").assertExists()

        composeRule.onNodeWithText("Filosofía").assertExists()
        composeRule.onNodeWithText("Literatura").assertExists()
        composeRule.onNodeWithText("Ciencia").assertExists()
    }

    @Test
    fun homeScreen_filtersDates_whenCategoryIsSelected() {
        // Arrange
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = HomeViewModel(fakeRepository)

        composeRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Wait to load data
        composeRule.waitUntil(5_000) {
            viewModel.filteredDates.value.isNotEmpty()
        }

        // Act: click category "Filosofía"
        composeRule.onNodeWithText("Filosofía")
            .assertExists()
            .performClick()

        // Assert: filters by category
        composeRule.onNodeWithText("getDatesByCategory Tag 1").assertExists()
        composeRule.onNodeWithText("getDatesByCategory Tag 2").assertExists()
        composeRule.onNodeWithText("getDatesByCategory Tag 3").assertDoesNotExist()
        composeRule.onNodeWithText("getDatesByCategory Tag 4").assertDoesNotExist()
    }

    @Test
    fun homeScreen_filtersDates_whenSearchQueryIsTyped() {
        // Arrange
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = HomeViewModel(fakeRepository)

        composeRule.setContent {
            HomeScreen(
                navController = rememberNavController(),
                viewModel = viewModel
            )
        }

        // Wait to load data
        composeRule.waitUntil(5_000) {
            viewModel.filteredDates.value.isNotEmpty()
        }

        // Act: type on the search bar
        composeRule.onNode(hasText("Buscar..."))
            .performTextInput("Tag 1")

        // Assert
        composeRule.onNodeWithText("getDates Tag 1").assertExists()
        composeRule.onNodeWithText("getDates Tag 2").assertDoesNotExist()
        composeRule.onNodeWithText("getDates Tag 3").assertDoesNotExist()
    }

    @Test
    fun homeScreen_navigatesToArticle_whenDateIsClicked() {
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = HomeViewModel(fakeRepository)

        composeRule.setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {
                composable("home") {
                    HomeScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable("articles/{id}") {}
            }
        }

        // Wait to load data
        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.filteredDates.value.isNotEmpty()
        }

        // Click on the first date
        composeRule.onNodeWithText("getDates Tag 1").performClick()
    }

}