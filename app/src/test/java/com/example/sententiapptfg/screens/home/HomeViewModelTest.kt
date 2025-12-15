package com.example.sententiapptfg.screens.home

import com.example.sententiapptfg.data.FakeSententiAppRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        viewModel = HomeViewModel(
            repository = FakeSententiAppRepository(),
            dispatcher = mainDispatcherRule.dispatcher
        )
    }

    @Test
    fun `loadDates loads fake data successfully`() = runTest {
        viewModel.loadDates()
        advanceUntilIdle()
        val result = viewModel.dates.value
        assertEquals(3, result.size)
        assertEquals("getDates Tag 1", result.first().tag)
    }

    @Test
    fun `loadCategories loads fake categories successfully`() = runTest {
        viewModel.loadCategories()
        advanceUntilIdle()
        val result = viewModel.categories.value
        assertEquals(3, result.size)
        assertEquals("Filosofía", result.first())
    }

    @Test
    fun `loadDatesByCategory with null loads all dates`() = runTest {
        viewModel.loadDatesByCategory(null)
        advanceUntilIdle()
        val result = viewModel.filteredDates.value
        assertEquals(3, result.size)
        assertEquals("getDates Tag 1", result.first().tag)
    }

    @Test
    fun `loadDatesByCategory with valid category loads filtered dates`() = runTest {
        viewModel.loadDatesByCategory("Filosofía")
        advanceUntilIdle()
        val result = viewModel.filteredDates.value
        assertEquals(2, result.size)
        assertEquals("getDatesByCategory Tag 1", result.first().tag)
    }


}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}