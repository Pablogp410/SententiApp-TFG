package com.example.sententiapptfg.screens

import androidx.compose.runtime.mutableStateListOf
import com.example.sententiapptfg.data.Quote

object UserInteractions {
    val likedQuotes = mutableStateListOf<Quote>()
    val favoriteQuotes = mutableStateListOf<Quote>()
    val funnyQuotes = mutableStateListOf<Quote>()
    val dislikedQuotes = mutableStateListOf<Quote>()

    val allQuotes: List<Quote>
        get() = (likedQuotes + favoriteQuotes + funnyQuotes + dislikedQuotes).distinct()
}