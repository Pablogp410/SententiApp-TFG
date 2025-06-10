package com.example.sententiapptfg.data

data class Article(
    val id: String,
    val title: String,
    val description: String,
    val quotes: List<Quote>,
    val date: String,
    val imagePath: String,
    val category: ArticleCategory
)
