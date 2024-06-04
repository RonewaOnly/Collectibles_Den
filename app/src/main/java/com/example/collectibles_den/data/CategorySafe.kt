package com.example.collectibles_den.data

data class CategorySafe(
    val category: String,
    val cover : String = "",
    val collectionItems: List<MakeCollection> = emptyList(),
)
