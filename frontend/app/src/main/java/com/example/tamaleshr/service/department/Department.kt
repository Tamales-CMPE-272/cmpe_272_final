package com.example.tamaleshr.service.department

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Department(
    @Json(name = "department")
    val departmentInfo: DepartmentInfo,
    @Json(name = "departmentEmployee")
    val departmentEmployees: List<DepartmentEmployee>,
    @Json(name = "deptManager")
    val departmentManagers: List<DepartmentManager>
){
    val manager: DepartmentManager?
        get() = departmentManagers.firstOrNull()
}

@JsonClass(generateAdapter = true)
data class DepartmentEmployee(
    val currentlyEnrolled: Boolean = false,
    val employee: DepartmentEmployeeData
)

@JsonClass(generateAdapter = true)
data class DepartmentEmployeeData(
    @Json(name = "emp_no")
    val employeeNo: Int,
    @Json(name = "first_name")
    val firsName: String,
    @Json(name = "last_name")
    val lastName: String
)

@JsonClass(generateAdapter = true)
data class DepartmentManager(
    val currentlyEnrolled: Boolean = false,
    val employee: DepartmentEmployeeData
)

@JsonClass(generateAdapter = true)
data class DepartmentInfo(
    @Json(name = "dept_no")
    val departmentNo: String,
    @Json(name = "dept_name")
    val departmentName: String
)