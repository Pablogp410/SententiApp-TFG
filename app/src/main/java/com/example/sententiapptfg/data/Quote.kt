package com.example.sententiapptfg.data

data class Quote(
    val id: String,
    val latinQuote: String,
    val spanishQuote: String,
    val englishQuote: String,
    val author: String,
    val likes: Int,
    val favorites: Int,
    val dislikes: Int,
    val funny: Int
)
