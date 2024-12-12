package com.example.capstone.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.data.UserRepository
import com.example.capstone.data.local.entity.PredictAndVideos
import kotlinx.coroutines.launch

class HistoryViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getAllPredictAndVideos(): LiveData<List<PredictAndVideos>> = userRepository.getAllPredictAndVideos()
    fun deleteAllPredictAndVideos() {
        viewModelScope.launch {
            userRepository.deleteAll()
        }
    }
}