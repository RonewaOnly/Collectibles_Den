package com.example.collectibles_den.Data

data class SearchData(
    var title: String,
    var customerId: String = "",
    var filter: () -> Unit
)
