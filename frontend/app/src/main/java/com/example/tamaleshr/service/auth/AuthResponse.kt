package com.example.tamaleshr.service.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AuthResponse(
    val access_token: String? = null,
    val expires_in: Int? = null,
    val refresh_expires_in: Int? = null,
    val refresh_token: String? = null,
    val token_type: String? = null,
    val session_state: String? = null,
    val scope: String? = null,
)