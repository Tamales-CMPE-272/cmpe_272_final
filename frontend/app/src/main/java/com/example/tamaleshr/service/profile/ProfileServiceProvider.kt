package com.example.tamaleshr.service.profile

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.ServiceProvider
import retrofit2.Retrofit

class ProfileServiceProvider: ServiceProvider<ProfileService> {
    override val retrofit: Retrofit get() = koin.get()

    override fun service(): ProfileService {
        return retrofit.create(ProfileService::class.java)
    }
}