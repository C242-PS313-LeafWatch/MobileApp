package com.example.capstone.ui.signup

import androidx.lifecycle.ViewModel
import com.example.capstone.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(username: String, email: String, password: String, confPassword: String, gender: String, birthDate: String) = repository.register(
        username = username, email = email, password = password, confPassword = confPassword, gender = gender, birthDate = birthDate)
}