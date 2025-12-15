package com.example.sententiapptfg.screens.article

import com.example.sententiapptfg.data.FakeSententiAppRepository
import com.example.sententiapptfg.data.models.Quote
import com.example.sententiapptfg.screens.home.MainDispatcherRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ArticleViewModel

    @Before
    fun setup() {
        viewModel = ArticleViewModel(
            repository = FakeSententiAppRepository(),
            dispatcher = mainDispatcherRule.dispatcher
        )
    }

    @Test
    fun `loadDateDetails loads correct date`() = runTest {
        viewModel.loadDateDetails(2)
        advanceUntilIdle()
        val result = viewModel.dateDetails.value
        assertNotNull(result)
        assertEquals(2, result!!.id)
        assertEquals("getDateDetails Tag 2", result.tag)
        assertEquals("getDateDetails details 2", result.details)
    }

    @Test
    fun `loadQuotes loads quotes for date`() = runTest {
        viewModel.loadQuotes(1)
        advanceUntilIdle()
        val result = viewModel.quotes.value
        assertEquals(2, result.size)
        assertTrue(result[0].author.contains("getQuotes author"))
    }

    @Test
    fun `loadQuoteDetails invokes callback with quote`() = runTest {
        var loadedQuote: Quote? = null
        viewModel.loadQuoteDetails("1") {
            loadedQuote = it
        }
        advanceUntilIdle()
        assertNotNull(loadedQuote)
        assertEquals("1", loadedQuote!!.id)
        assertTrue(loadedQuote!!.author.contains("getQuoteDetails author"))
    }

    @Test
    fun `loadOtherDates filters out excluded id`() = runTest {
        viewModel.loadOtherDates(excludeId = 2)
        advanceUntilIdle()
        val result = viewModel.otherDates.value
        assertEquals(2, result.size)
        assertFalse(result.any { it.id == 2 })
    }

    @Test
    fun `loadDateDetails updates loading state correctly`() = runTest {
        viewModel.loadDateDetails(1)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading.value)
    }
}
