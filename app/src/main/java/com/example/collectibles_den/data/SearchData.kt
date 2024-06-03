package com.example.collectibles_den.data

data class SearchData(
    var title: String,
    var customerId: String = "",
    var filter: () -> Unit
)
