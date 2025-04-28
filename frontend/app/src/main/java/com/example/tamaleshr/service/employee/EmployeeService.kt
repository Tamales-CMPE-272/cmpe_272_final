package com.example.tamaleshr.service.employee

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EmployeeService {
    @GET("/tamalesHr/employees/{employeeId}")
    fun fetchGradesByAssignment(
        @Path("employeeId") employeeId: String,
    ): Call<Employee>
}