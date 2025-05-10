package com.example.tamaleshr.service.salary

import com.squareup.moshi.JsonClass
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class Salary(
    val empNo: Long? = null,
    val fromDate: LocalDate? = null,
    val toDate: LocalDate? = null,
    val salary: Double? = null
)