package com.example.capstone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.capstone.data.UserRepository
import com.example.capstone.data.local.pref.UserModel

// ViewModel untuk MainActivity
class MainViewModel(private val repository: UserRepository) : ViewModel() {
    // Mengambil sesi user dan mengkonversi Flow ke LiveData
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }
}