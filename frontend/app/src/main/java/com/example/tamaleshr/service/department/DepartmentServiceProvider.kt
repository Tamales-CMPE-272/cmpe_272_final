package com.example.tamaleshr.service.department

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.ServiceProvider
import retrofit2.Retrofit

class DepartmentServiceProvider: ServiceProvider<DepartmentService> {
    override val retrofit: Retrofit get() = koin.get()

    override fun service(): DepartmentService {
        return retrofit.create(DepartmentService::class.java)
    }
}