package com.example.sententiapptfg.screens.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sententiapptfg.data.SententiAppRepository
import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleViewModel(private val repository: SententiAppRepository) : ViewModel() {
    private val _dateDetails = MutableStateFlow<Date?>(null)
    val dateDetails: StateFlow<Date?> = _dateDetails

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _otherDates = MutableStateFlow<List<Date>>(emptyList())
    val otherDates: StateFlow<List<Date>> = _otherDates

    fun loadDateDetails(dateId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val date = repository.getDateDetails(dateId)
                _dateDetails.value = date
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadQuotes(dateId: Int) {
        viewModelScope.launch {
            try {
                val quotes = repository.getQuotes(dateId)
                _quotes.value = quotes
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun loadQuoteDetails(quoteId: String, onLoaded: (Quote?) -> Unit) {
        viewModelScope.launch {
            try {
                val quote = repository.getQuoteDetails(quoteId)
                onLoaded(quote)
            } catch (e: Exception) {
                e.printStackTrace()
                onLoaded(null)
            }
        }
    }

    fun loadOtherDates(excludeId: Int) {
        viewModelScope.launch {
            try {
                val allDates = withContext(Dispatchers.IO) {
                    repository.getDates()
                }
                val filtered = allDates
                    .filter { it.id != excludeId }
                    .shuffled()
                    //.take(3)
                _otherDates.value = filtered
            } catch (e: Exception) {
                e.printStackTrace()
                _otherDates.value = emptyList()
            }
        }
    }
}