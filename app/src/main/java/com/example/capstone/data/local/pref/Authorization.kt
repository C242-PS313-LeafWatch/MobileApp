package com.example.capstone.data.local.pref

import okhttp3.Interceptor
import okhttp3.Response

class Authorization: Interceptor {
    private var token: String? = null

    fun setToken(newToken: String?) {
        token = newToken
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return token?.let { nonNullToken ->
            val newRequest = request.newBuilder()
                .addHeader("Authorization", " Bearer $nonNullToken")
                .build()
            chain.proceed(newRequest)
        } ?: chain.proceed(request)
    }

}