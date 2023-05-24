package com.example.mvvmdemoexample.login

data class LoginModal(
    val status: Boolean,
    val message: String,
    val data: Data
) {
    inner class Data(
        val id: Int?,
        val name: String,
        val email: String
    )
}