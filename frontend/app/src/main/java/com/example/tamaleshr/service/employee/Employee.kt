package com.example.tamaleshr.service.employee

import com.example.tamaleshr.service.department.DepartmentManager
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class Employee(
    val emp_no: Int? = null,
    val birth_date: Date? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val hire_date: Date? = null,
    val deptManagers: List<EmployeeManagementData>? = null
){
    fun initials(): String {
        val firstInitial = first_name?.trim()?.firstOrNull()?.uppercase().orEmpty()
        val lastInitial = last_name?.trim()?.firstOrNull()?.uppercase().orEmpty()
        return "$firstInitial$lastInitial"
    }

    fun fullName(): String {
        return "$first_name $last_name"
    }

    val managerData: EmployeeManagementData?
        get() = if(!deptManagers.isNullOrEmpty()){
            deptManagers.firstOrNull()
        } else null
}