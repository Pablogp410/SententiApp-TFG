package com.example.sententiapptfg.screens.article

import android.content.Context
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sententiapptfg.R
import com.example.sententiapptfg.data.EmptyQuotesFakeRepository
import com.example.sententiapptfg.data.FakeSententiAppRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleScreenKtTest{

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        navController = TestNavHostController(context)
        navController.navigatorProvider.addNavigator(ComposeNavigator())
    }

    @Test
    fun articleScreen_showsLoadingAndThenContent() {
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = ArticleViewModel(fakeRepository)

        composeRule.setContent {
            CompositionLocalProvider(LocalArticleViewModel provides viewModel) {
                ArticleScreen(navController = rememberNavController(), viewModel = viewModel, dateId = 1)
            }
        }

        // CircularProgressIndicator should exist while loading data
        composeRule.onNodeWithTag("loading").assertExists()

        // Wait to load data
        composeRule.waitUntil(timeoutMillis = 5000) {
            viewModel.dateDetails.value != null
        }

        // Assert: checks the data is loaded correctly
        composeRule.onNodeWithText("getDateDetails Tag 1").assertExists()

        // Wait to load quotes
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("getQuoteDetails latinQuote 1").fetchSemanticsNodes().isNotEmpty() &&
                    composeRule.onAllNodesWithText("getQuoteDetails latinQuote 2").fetchSemanticsNodes().isNotEmpty()
        }

        // Assert: checks the quotes are loaded correctly
        composeRule.onNodeWithText("getQuoteDetails latinQuote 1").assertExists()
        composeRule.onNodeWithText("getQuoteDetails latinQuote 2").assertExists()

        composeRule.onNodeWithText("getQuoteDetails author 1").assertExists()
        composeRule.onNodeWithText("getQuoteDetails author 2").assertExists()
    }

    @Test
    fun articleScreen_navigatesBack_whenBackPressed() {
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = ArticleViewModel(fakeRepository)

        composeRule.setContent {
            NavHost(
                navController = navController,
                startDestination = "article/1"
            ) {
                composable("home") { }
                composable("article/{id}") { backStackEntry ->
                    CompositionLocalProvider(LocalArticleViewModel provides viewModel) {
                        ArticleScreen(
                            navController = navController,
                            viewModel = viewModel,
                            dateId = 1
                        )
                    }
                }
            }
        }

        // Wait to load data
        composeRule.waitUntil(5_000) {
            viewModel.dateDetails.value != null
        }

        // Click ArrowBack button
        composeRule.onNodeWithContentDescription("ArrowBack")
            .assertExists()
            .performClick()

        // Verify popBackStack
        assert(navController.previousBackStackEntry != null)
    }

    @Test
    fun articleScreen_rendersCorrectNumberOfQuotes() {
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = ArticleViewModel(fakeRepository)

        composeRule.setContent {
            CompositionLocalProvider(LocalArticleViewModel provides viewModel) {
                ArticleScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel,
                    dateId = 1
                )
            }
        }

        composeRule.waitUntil(5_000) {
            viewModel.quotes.value.size == 2
        }

        composeRule.onAllNodesWithText(
            text = "getQuoteDetails latinQuote",
            substring = true
        ).assertCountEquals(2)
    }

    @Test
    fun articleScreen_showsQuotesList_whenQuotesAreLoaded() {
        val fakeRepository = FakeSententiAppRepository()
        val viewModel = ArticleViewModel(fakeRepository)

        composeRule.setContent {
            CompositionLocalProvider(LocalArticleViewModel provides viewModel) {
                ArticleScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel,
                    dateId = 1
                )
            }
        }

        // Wait to load data
        composeRule.waitUntil(timeoutMillis = 5_000) {
            viewModel.quotes.value.isNotEmpty()
        }

        // Verify quote list exists
        composeRule
            .onNodeWithTag("quote_list")
            .assertExists()
            .assertIsDisplayed()
    }


    @Test
    fun articleScreen_showsEmptyState_whenNoQuotes() {
        val fakeRepository = EmptyQuotesFakeRepository()
        val viewModel = ArticleViewModel(fakeRepository)

        composeRule.setContent {
            CompositionLocalProvider(LocalArticleViewModel provides viewModel) {
                ArticleScreen(
                    navController = rememberNavController(),
                    viewModel = viewModel,
                    dateId = 1
                )
            }
        }

        composeRule.waitUntil(5_000) {
            viewModel.dateDetails.value != null
        }

        composeRule.onNodeWithText("No hay sentencias disponibles")
            .assertExists()
    }


}