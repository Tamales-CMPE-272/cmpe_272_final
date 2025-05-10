package com.example.tamaleshr.service.salary

import com.example.tamaleshr.di.serviceModule
import com.example.tamaleshr.service.Repository
import com.example.tamaleshr.service.ServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response

class SalaryRepository(
    override val provider: ServiceProvider<SalaryService>
) : Repository<SalaryService> {

    fun findSalaryByEmployeeId(id: String): Response<List<Salary>>? {
        val response = provider.service().fetchSalariesByEmployeeId(
            employeeId = id
        ).execute()
        return if (response.isSuccessful) {
            response
        } else {
            val error = response.errorBody()
                ?: throw IllegalAccessException("Response did not return error")
            Response.error(response.code(), error)
        }
    }

    companion object {
        fun koinModule(): Module {
            return module {
                includes(serviceModule)
                single {
                    SalaryRepository(
                        provider = SalaryServiceProvider()
                    )
                }
            }
        }
    }

}