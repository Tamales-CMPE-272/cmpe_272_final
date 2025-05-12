package com.example.tamaleshr.service.auth

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.ServiceProvider
import retrofit2.Retrofit

class AuthServiceProvider: ServiceProvider<AuthService> {
    override val retrofit: Retrofit
        get() = koin.get()

    override fun service(): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}