package com.example.tamaleshr.service.department

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface DepartmentService {
    @GET("/tamalesHr/department/{departmentId}")
    fun fetchDepartmentById(
        @Path("departmentId") deptId: String,
    ): Call<Department>

    @PUT("/tamalesHr/department/remove/{emp_no}/{dept_no}")
    fun removeUserFromDept(
        @Path("emp_no") employeeId: String,
        @Path("dept_no") deptId: String,
    ): Call<Department>

    @PUT("/tamalesHr/department/update/{emp_no}/{dept_no}")
    fun addUserToDept(
        @Path("emp_no") employeeId: String,
        @Path("dept_no") deptId: String,
    ): Call<Department>
}