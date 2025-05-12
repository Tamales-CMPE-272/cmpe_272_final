package com.example.tamaleshr.service.department

import com.example.tamaleshr.di.serviceModule
import com.example.tamaleshr.service.Repository
import com.example.tamaleshr.service.ServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response

class DepartmentRepository(
    override val provider: ServiceProvider<DepartmentService>
): Repository<DepartmentService> {

    suspend fun findDepartmentById(deptId: String): Response<Department>{
        val response = provider.service().fetchDepartmentById(deptId).execute()
        return if(response.isSuccessful){
            response
        } else {
            val error = response.errorBody()
                ?: throw IllegalAccessException("Response did not return error")
            Response.error(response.code(), error)
        }
    }

    suspend fun removeUser(deptId: String, employeeId: Int): Response<Department> {
        val response = provider.service().removeUserFromDept(
            employeeId = employeeId.toString(),
            deptId = deptId
        ).execute()
        return if (response.isSuccessful) {
            response
        } else {
            val error = response.errorBody()
                ?: throw IllegalAccessException("Response did not return error")
            Response.error(response.code(), error)
        }
    }

    suspend fun addUser(deptId: String, employeeId: Int): Response<Department> {
        val response = provider.service().addUserToDept(
            employeeId = employeeId.toString(),
            deptId = deptId
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
                    DepartmentRepository(
                        provider = DepartmentServiceProvider()
                    )
                }
            }
        }
    }
}