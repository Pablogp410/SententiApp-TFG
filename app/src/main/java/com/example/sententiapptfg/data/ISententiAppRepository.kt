package com.example.sententiapptfg.data

import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote

interface ISententiAppRepository {

    suspend fun getDates(lang: String = "es"): List<Date>

    suspend fun getCategories(lang: String = "es"): List<String>

    suspend fun getDatesByCategory(category: String, lang: String = "es"): List<Date>

    suspend fun getDateDetails(id: Int, lang: String = "es"): Date

    suspend fun getQuotes(id: Int, lang: String = "es"): List<Quote>

    suspend fun getQuoteDetails(id: String, lang: String = "es"): Quote
}