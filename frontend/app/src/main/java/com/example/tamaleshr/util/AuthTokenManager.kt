package com.example.tamaleshr.util

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.tamaleshr.service.auth.AuthResponse
import androidx.core.content.edit
import com.example.tamaleshr.service.auth.JwtTokenPayload
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.joda.time.DateTime
import org.joda.time.Duration
import java.util.Date

class AuthTokenManager(context: Context) {

    companion object {
        val KEY_AUTH_PREFS = "auth_prefs"
        val KEY_ACCESS_TOKEN = "access_token"
        val KEY_REFRESH_TOKEN = "refresh_token"
        val KEY_EMP_NO = "username"
        val KEY_LAST_TOKEN = "KEY_LAST_TOKEN"
        val KEY_EXPIRES_IN = "KEY_EXPIRES_IN"
    }

    private val sharedPreferences = EncryptedSharedPreferences.create(
        KEY_AUTH_PREFS,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(auth: AuthResponse) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, auth.access_token)
                .putString(KEY_REFRESH_TOKEN, auth.refresh_token)
                .putString(KEY_LAST_TOKEN, DateTime.now().toLocalDateTime().toString())
                .putInt(KEY_EXPIRES_IN, auth.expires_in ?: 0)
        }
    }

    fun isTokenValid(): Boolean{
        val timestamp = sharedPreferences.getString(KEY_LAST_TOKEN, null) ?: return true
        val expiresIn = sharedPreferences.getInt(KEY_EXPIRES_IN, 0)

       val timeLastSaved = DateTime.parse(timestamp)
        val now = DateTime.now()

        val duration = Duration(timeLastSaved, now)
        return duration.standardSeconds < expiresIn
    }

    fun saveUsername(username: String){
        sharedPreferences.edit {
            putString(KEY_EMP_NO, username)
        }
    }

    fun getAccessToken(): String? = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)

    fun getRefreshToken(): String? = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)

    fun getUsername(): String = sharedPreferences.getString(KEY_EMP_NO, null).orEmpty()

    fun clear() = sharedPreferences.edit { clear() }

    fun decodeJwtPayloadToModel(): JwtTokenPayload? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build() ?: return null
        val token = getAccessToken() ?: return null
        val parts = token.split(".")
        if (parts.size != 3) return null

        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val json = String(decodedBytes, Charsets.UTF_8)

        val adapter = moshi.adapter(JwtTokenPayload::class.java)
        return adapter.fromJson(json)
    }
}