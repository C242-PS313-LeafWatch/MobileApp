package com.example.capstone.data.local.pref

data class UserModel(
    val email: String,
    val password: String,
    val token: String ,
    val isLogin: Boolean = false
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val confPassword: String,
    val gender: String,
    val birthdate: String
)

data class LoginRequest(
    val email: String,
    val password: String
)
