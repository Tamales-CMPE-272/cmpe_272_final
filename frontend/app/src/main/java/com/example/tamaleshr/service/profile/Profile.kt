package com.example.tamaleshr.service.profile

import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
class Profile(
    val emp_no: Int? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val gender: Int? = null,
    val birth_date: Date? = null,
    val hire_date: Date? = null,
    val title: String? = null,
    val department_name: String? = null,
){
    fun initials(): String {
        val firstInitial = first_name?.trim()?.firstOrNull()?.uppercase().orEmpty()
        val lastInitial = last_name?.trim()?.firstOrNull()?.uppercase().orEmpty()
        return "$firstInitial$lastInitial"
    }

    fun fullName(): String {
        return "$first_name $last_name"
    }
}