package com.example.capstone.ui.camera

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.capstone.data.UserRepository
import okhttp3.MultipartBody

class CameraViewModel(private val userRepository: UserRepository): ViewModel() {
    fun uploadFile(file: MultipartBody.Part, currentImg: Uri?) = userRepository.uploadFile(file, currentImg)
}