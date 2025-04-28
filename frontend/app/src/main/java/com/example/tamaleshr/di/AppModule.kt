package com.example.tamaleshr.di

import com.example.tamaleshr.service.employee.EmployeeRepository
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

val appModule = module {
    includes(serviceModule)
    includes(EmployeeRepository.koinModule())
}

val koin: Koin
    get() = GlobalContext.get()