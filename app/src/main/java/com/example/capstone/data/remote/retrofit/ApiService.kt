package com.example.capstone.data.remote.retrofit

import com.example.capstone.data.local.pref.LoginRequest
import com.example.capstone.data.local.pref.RegisterRequest
import com.example.capstone.data.remote.response.FileUploadResponse
import com.example.capstone.data.remote.response.LoginResponse
import com.example.capstone.data.remote.response.ProfileResponse
import com.example.capstone.data.remote.response.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("users")
    suspend fun register(
        @Body registerRequest : RegisterRequest
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @Multipart
    @POST("predict/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part
    ): FileUploadResponse
}