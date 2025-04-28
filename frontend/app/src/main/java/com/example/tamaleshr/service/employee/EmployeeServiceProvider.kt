package com.example.tamaleshr.service.employee

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.ServiceProvider
import retrofit2.Retrofit

class EmployeeServiceProvider: ServiceProvider<EmployeeService> {
    override val retrofit: Retrofit get() = koin.get()

    override fun service(): EmployeeService {
       return retrofit.create(EmployeeService::class.java)
    }
}