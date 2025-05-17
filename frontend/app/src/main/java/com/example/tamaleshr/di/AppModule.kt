package com.example.tamaleshr.di

import android.content.Context
import com.example.tamaleshr.service.auth.AuthRepository
import com.example.tamaleshr.service.department.Department
import com.example.tamaleshr.service.department.DepartmentRepository
import com.example.tamaleshr.service.employee.EmployeeRepository
import com.example.tamaleshr.service.profile.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

fun appModule(context: Context) = module {
    includes(serviceModule)
    includes(authTokenManager(context))
    includes(AuthRepository.koinModule())
    includes(EmployeeRepository.koinModule())
    includes(ProfileRepository.koinModule())
    includes(DepartmentRepository.koinModule())
    includes(dispatchers())
}

fun dispatchers() = module {
    single {
        DispatcherProviderImpl() as DispatcherProvider
    }
}

class DispatcherProviderImpl(
    override val io: CoroutineDispatcher = Dispatchers.IO,
    override val main: CoroutineDispatcher = Dispatchers.Main,
    override val default: CoroutineDispatcher = Dispatchers.Default,
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
) : DispatcherProvider

val koin: Koin
    get() = GlobalContext.get()