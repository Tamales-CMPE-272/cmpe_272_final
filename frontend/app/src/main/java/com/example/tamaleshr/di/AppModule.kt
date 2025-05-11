package com.example.tamaleshr.di

import android.content.Context
import com.example.tamaleshr.service.auth.AuthRepository
import com.example.tamaleshr.service.employee.EmployeeRepository
import com.example.tamaleshr.service.profile.ProfileRepository
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

fun appModule(context: Context) = module {
    includes(serviceModule)
    includes(authTokenManager(context))
    includes(AuthRepository.koinModule())
    includes(EmployeeRepository.koinModule())
    includes(ProfileRepository.koinModule())
}

val koin: Koin
    get() = GlobalContext.get()