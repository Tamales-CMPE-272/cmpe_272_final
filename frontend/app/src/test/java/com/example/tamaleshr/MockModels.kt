package com.example.tamaleshr

import android.content.Context
import android.content.SharedPreferences
import com.example.tamaleshr.service.auth.Access
import com.example.tamaleshr.service.auth.AuthResponse
import com.example.tamaleshr.service.auth.JwtTokenPayload
import com.example.tamaleshr.service.auth.ResourceAccess
import com.example.tamaleshr.service.employee.EmployeeManagementData
import com.example.tamaleshr.util.AuthTokenManager
import com.squareup.moshi.Json
import io.mockk.mockk

class FakeAuthTokenManager(
    context: Context,
) : AuthTokenManager(context, mockk(relaxed = true)) {
    override fun getAccessToken(): String {
        return "fake_access_token"
    }

    override fun saveTokens(auth: AuthResponse) {

    }

    override fun saveDeptId(data: EmployeeManagementData?) {

    }

    override fun isTokenValid(): Boolean {
       return true
    }

    override fun saveUsername(username: String) {

    }

    override fun deptId(): String? {
       return "fake_dept_id"
    }

    override fun getRefreshToken(): String? {
       return "fake_refresh_token"
    }

    override fun getUsername(): String {
        return "fake_username"
    }

    override fun clear() {

    }
}