package com.example.tamaleshr.usecase.department

import com.example.tamaleshr.service.department.Department
import com.example.tamaleshr.service.department.DepartmentRepository
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCase
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.defaultFailureFlow
import com.example.tamaleshr.usecase.successFlow
import kotlinx.coroutines.flow.Flow

class DepartmentUseCase(
    private val departmentId: String,
    override val repository: DepartmentRepository
) : UseCase<Department, DepartmentRepository> {

    override suspend fun launch(): Flow<UseCaseResult<Department, DefaultError>> {
        return try {
            val response = repository.findDepartmentById(departmentId)
            val department = response.body()
            val errorResponse = response.errorBody()
            when{
                department != null -> successFlow(department)
                errorResponse != null -> defaultFailureFlow(code = response.code(), errorResponse)
                else -> defaultFailureFlow(response)
            }
        }catch(e: Exception){
            defaultFailureFlow(e)
        }
    }
}