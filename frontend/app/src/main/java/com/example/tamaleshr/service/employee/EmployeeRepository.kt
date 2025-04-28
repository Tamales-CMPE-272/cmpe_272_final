package com.example.tamaleshr.service.employee

import com.example.tamaleshr.di.serviceModule
import com.example.tamaleshr.service.Repository
import com.example.tamaleshr.service.ServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response

class EmployeeRepository(
    override val provider: ServiceProvider<EmployeeService>
) : Repository<EmployeeService> {

    suspend fun findEmployeeById(id: String): Response<Employee> {
        val response = provider.service().fetchGradesByAssignment(
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
                    EmployeeRepository(
                        provider = EmployeeServiceProvider()
                    )
                }
            }
        }
    }

}