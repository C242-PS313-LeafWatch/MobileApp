package com.example.capstone.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.Result
import com.example.capstone.data.UserRepository
import com.example.capstone.data.remote.response.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getProfile(): LiveData<Result<User?>> = repository.getProfile()
}