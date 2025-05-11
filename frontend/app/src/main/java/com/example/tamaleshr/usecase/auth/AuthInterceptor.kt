package com.example.tamaleshr.usecase.auth

import android.content.SharedPreferences
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.util.AuthTokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenManager: AuthTokenManager = koin.get()
) : Interceptor {
    val accessToken: String?
        get() = tokenManager.getAccessToken()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return if (chain.request().url.pathSegments.any { it.contains("auth") }) {
            chain.proceed(request)
        } else if (!accessToken.isNullOrBlank() && tokenManager.isTokenValid()) {
            chain.proceed(
                request.newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            )
        } else {
            chain.proceed(request)
        }
    }
}