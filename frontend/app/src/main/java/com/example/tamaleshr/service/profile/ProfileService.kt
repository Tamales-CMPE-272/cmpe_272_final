package com.example.tamaleshr.service.profile

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {
    @GET("/tamalesHr/profile/{employeeId}")
    fun fetchGradesByAssignment(
        @Path("employeeId") employeeId: String,
    ): Call<Profile>
}