package com.example.capstone.ui.login

import androidx.lifecycle.ViewModel
import com.example.capstone.data.UserRepository

// Menyimpan sesi user melalui repository
class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    fun login(email: String, password: String) = repository.login(email, password)
}