package com.example.capstone.data

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.capstone.data.local.entity.HistoryEntity
import com.example.capstone.data.local.entity.ListHistoryEntity
import com.example.capstone.data.local.entity.PredictAndVideos
import com.example.capstone.data.local.entity.PredictVideosCrossRef
import com.example.capstone.data.local.pref.LoginRequest
import com.example.capstone.data.local.pref.RegisterRequest
import com.example.capstone.data.local.pref.UserModel
import com.example.capstone.data.local.pref.UserPreference
import com.example.capstone.data.local.room.HistoryDao
import com.example.capstone.data.remote.response.FileUploadResponse
import com.example.capstone.data.remote.response.LoginResponse
import com.example.capstone.data.remote.response.ProfileResponse
import com.example.capstone.data.remote.response.RegisterResponse
import com.example.capstone.data.remote.response.User
import com.example.capstone.data.remote.retrofit.ApiConfig
import com.example.capstone.data.remote.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.net.SocketTimeoutException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val apiAuth: ApiService,
    private val dao: HistoryDao
) {
    fun register(username: String, email: String, password:String, confPassword: String, gender: String, birthDate: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiAuth.register(RegisterRequest(username= username, email= email, password= password, confPassword= confPassword, gender= gender, birthdate= birthDate))
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
            val errorMessage = errorBody.msg
            emit(Result.Error(errorMessage))
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiAuth.login(LoginRequest(email, password))
            val token = response.accessToken
            ApiConfig.updateToken(token)
            saveSession(
                UserModel(
                    email = email,
                    password = password,
                    token = response.accessToken ?: "",
                    isLogin = true
                )
            )
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
            val errorMessage = errorBody.msg
            emit(Result.Error(errorMessage))
        }
    }

    fun getProfile(): LiveData<Result<User?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiAuth.getProfile()
            val profile = response.user
            emit(Result.Success(profile))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ProfileResponse::class.java)
            val errorMessage = errorBody.msg
            emit(Result.Error(errorMessage))
        }
    }

    fun uploadFile(file: MultipartBody.Part, currentImg: Uri?): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val lastGroup = dao.getLastGroup() ?: 0
            val newGroup = lastGroup + 1
            val response = apiService.uploadImage(file)
            emit(Result.Success(response))
            val analyzeList =
                HistoryEntity(
                    imageUri = currentImg.toString(),
                    keterangan = response.prediction.keterangan,
                    namaPenyakit = response.prediction.namaPenyakit,
                    tingkatPrediksi = response.prediction.tingkatPrediksi,
                    predictVideoId = newGroup
                )
            val videoList = response.videos.map { videos ->
                ListHistoryEntity(
                    videosId = newGroup,
                    thumbnail = videos.thumbnail,
                    videoUrl = videos.videoUrl,
                    description = videos.description,
                    title = videos.title
                )
            }
            val predictAndVideos = PredictVideosCrossRef(newGroup, newGroup)
            dao.insertHistory(analyzeList)
            dao.insertListVideo(videoList)
            dao.insertCrossRef(predictAndVideos)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, FileUploadResponse::class.java)
            val errorMessage = errorBody.status
            emit(Result.Error(errorMessage))
        } catch (e: SocketTimeoutException) {
            // Secara eksplisit tangani SocketTimeoutException
            emit(Result.Error("Koneksi timeout. Silakan coba lagi."))
        } catch (e: Exception) {
            // Tangkap semua exception lain
            emit(Result.Error(e.message ?: "Upload gagal"))
        }
    }

    fun getAllPredictAndVideos(): LiveData<List<PredictAndVideos>> = dao.getAllPredictAndVideos()

    suspend fun deleteAll()  = dao.deleteAll()

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession().onEach { user ->
        ApiConfig.updateToken(user.token.takeIf { user.isLogin })
    }

    suspend fun logout() {
        ApiConfig.updateToken(null)
        userPreference.logout()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            apiAuth: ApiService,
            dao: HistoryDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService, apiAuth, dao)
            }.also { instance = it }
    }
}