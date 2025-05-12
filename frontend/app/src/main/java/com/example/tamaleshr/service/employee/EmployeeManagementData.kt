package com.example.tamaleshr.service.employee

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EmployeeManagementData(
    @Json(name="emp_no")
    val empNo: Int,
    @Json(name="dept_no")
    val deptNo: String? = null,
    @Json(name="currentlyEnrolled")
    val currentlyEnrolled: Boolean
)