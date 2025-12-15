package com.example.sententiapptfg.screens.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sententiapptfg.data.models.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.State
import com.example.sententiapptfg.data.ISententiAppRepository
import kotlinx.coroutines.CoroutineDispatcher


class HomeViewModel(private val repository: ISententiAppRepository, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) : ViewModel() {

    private val _dates = mutableStateOf<List<Date>>(emptyList())
    val dates: State<List<Date>> = _dates

    private val _categories = mutableStateOf<List<String>>(emptyList())
    val categories: State<List<String>> = _categories

    private val _filteredDates = mutableStateOf<List<Date>>(emptyList())
    val filteredDates: State<List<Date>> = _filteredDates

    fun loadDates() {
        viewModelScope.launch {
            val newDates = withContext(dispatcher) {
                repository.getDates()
            }
            //Log.d("HomeViewModel", "Loaded dates: $newDates")
            _dates.value = newDates
            _filteredDates.value = newDates
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            val newCategories = withContext(dispatcher) {
                repository.getCategories()
            }
            newCategories.forEach { category ->
                //Log.d("HomeViewModel", "Categoría recibida: $category")
            }
            _categories.value = newCategories
        }
    }

    fun loadDatesByCategory(category: String?) {
        viewModelScope.launch {
            val newDates = withContext(dispatcher) {
                if (category == null) repository.getDates()
                else repository.getDatesByCategory(category)
            }
            //Log.d("HomeViewModel", "Loaded dates for category '$category': $newDates")
            _filteredDates.value = newDates
        }
    }
}
