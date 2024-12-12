package com.example.capstone.di

import android.content.Context
import com.example.capstone.data.UserRepository
import com.example.capstone.data.local.pref.UserPreference
import com.example.capstone.data.local.pref.dataStore
import com.example.capstone.data.local.room.HistoryDatabase
import com.example.capstone.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val database = HistoryDatabase.getDatabase(context)
        val apiAuth = ApiConfig.getApiServiceAuth()
        val dao = database.historyDao()
        return UserRepository.getInstance(pref, apiService, apiAuth, dao)
    }
}