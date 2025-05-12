package com.example.tamaleshr.service.auth

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("/auth/token")
    fun authToken(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String
    ): Call<AuthResponse>
}