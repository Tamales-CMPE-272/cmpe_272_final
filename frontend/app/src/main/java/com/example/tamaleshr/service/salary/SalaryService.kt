package com.example.tamaleshr.service.salary

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SalaryService {
    @GET("/tamalesHr/salary/{employeeId}")
    fun fetchSalariesByEmployeeId(
        @Path("employeeId") employeeId: String,
    ): Call<List<Salary>>
}