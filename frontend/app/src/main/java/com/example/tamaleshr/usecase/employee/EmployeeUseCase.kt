package com.example.tamaleshr.usecase.employee

import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.service.employee.EmployeeRepository
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCase
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.defaultFailureFlow
import com.example.tamaleshr.usecase.successFlow
import kotlinx.coroutines.flow.Flow

class EmployeeUseCase(
    private val employeeId: String,
    override val repository: EmployeeRepository
) : UseCase<Employee, EmployeeRepository> {
    override suspend fun launch(): Flow<UseCaseResult<Employee, DefaultError>> {
        return try {
            val response = repository.findEmployeeById(employeeId)
            val employee = response.body()
            val errorResponse = response.errorBody()
            when {
                employee != null -> successFlow(employee)
                errorResponse != null -> defaultFailureFlow(code = response.code(), errorResponse)
                else -> defaultFailureFlow(response)
            }
        } catch (e: Exception) {
            defaultFailureFlow(e)
        }
    }
}