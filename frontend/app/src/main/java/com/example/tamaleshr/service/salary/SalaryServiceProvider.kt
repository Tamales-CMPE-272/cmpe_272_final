package com.example.tamaleshr.service.salary

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.ServiceProvider
import retrofit2.Retrofit

class SalaryServiceProvider: ServiceProvider<SalaryService> {
    override val retrofit: Retrofit get() = koin.get()

    override fun service(): SalaryService {
        return retrofit.create(SalaryService::class.java)
    }
}