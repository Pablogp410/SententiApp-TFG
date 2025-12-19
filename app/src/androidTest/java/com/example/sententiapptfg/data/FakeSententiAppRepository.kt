package com.example.sententiapptfg.data

import com.example.sententiapptfg.data.models.Date
import com.example.sententiapptfg.data.models.Quote
import kotlinx.coroutines.delay

open class FakeSententiAppRepository : ISententiAppRepository {

    override suspend fun getDates(lang: String) = listOf(
        Date(id = 1, tag = "getDates Tag 1", image = "drawable/mockup.jpeg", day = "1 enero 2025", details = "getDates details 1"),
        Date(id = 2, tag = "getDates Tag 2", image = "drawable/mockup.jpeg", day = "2 enero 2025", details = "getDates details 2"),
        Date(id = 3, tag = "getDates Tag 3", image = "drawable/mockup.jpeg", day = "3 enero 2025", details = "getDates details 3"),
    )

    override suspend fun getCategories(lang: String) = listOf("Filosofía", "Literatura", "Ciencia")

    override suspend fun getDatesByCategory(category: String, lang: String) =
        listOf(
            Date(id = 1, tag = "getDatesByCategory Tag 1", image = "drawable/mockup.jpeg", day = "1 enero 2025", details = "getDatesByCategory details 1"),
            Date(id = 2, tag = "getDatesByCategory Tag 2", image = "drawable/mockup.jpeg", day = "2 enero 2025", details = "getDatesByCategory details 2"),
        )

    override suspend fun getDateDetails(id: Int, lang: String): Date {
        delay(300) //Delay to show circular progress indicator
        return Date(id = id, tag = "getDateDetails Tag $id", image = "drawable/mockup.jpeg", day = "1 enero 2025", details = "getDateDetails details $id")
    }


    override suspend fun getQuotes(id: Int, lang: String): List<Quote>{
        delay(300)
        return listOf(
            Quote(id= "1", author = "getQuotes author 1", article = "getQuotes article 1", latinQuote = "getQuotes latinQuote 1", spanishQuote = "getQuotes spanishQuote 1", englishQuote = "getQuotes englishQuote 1", category = "getQuotes category 1"),
            Quote(id= "2", author = "getQuotes author 2", article = "getQuotes article 2", latinQuote = "getQuotes latinQuote 2", spanishQuote = "getQuotes spanishQuote 2", englishQuote = "getQuotes englishQuote 2", category = "getQuotes category 2"),
        )
    }
    override suspend fun getQuoteDetails(id: String, lang: String) =
        Quote(id= id, author = "getQuoteDetails author $id", article = "getQuoteDetails article $id", latinQuote = "getQuoteDetails latinQuote $id", spanishQuote = "getQuoteDetails spanishQuote $id", englishQuote = "getQuoteDetails englishQuote $id", category = "getQuoteDetails category $id")
}

class EmptyQuotesFakeRepository : FakeSententiAppRepository() {
    override suspend fun getQuotes(id: Int, lang: String) = emptyList<Quote>()
}

