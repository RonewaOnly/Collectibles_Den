package com.example.collectibles_den.data

data class UserData(
    var id: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var email: String? = null,
    var username: String? = null,
    var password: String? = null
) {
    constructor() : this("", "", "", "", "", "")
}

