package com.example.tamaleshr.service.auth

import com.example.tamaleshr.di.serviceModule
import com.example.tamaleshr.service.Repository
import com.example.tamaleshr.service.ServiceProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Response
import retrofit2.http.Field

class AuthRepository(
    override val provider: ServiceProvider<AuthService>
) : Repository<AuthService> {

    suspend fun authToken(
        username: String, password: String, clientId: String, grantType: String
    ): Response<AuthResponse> {
        val response = provider.service().authToken(
            username = username,
            password = password,
            clientId = clientId,
            grantType = grantType
        ).execute()
        return if (response.isSuccessful) {
            response
        } else {
            val error = response.errorBody()
                ?: throw IllegalAccessException("Response did not return error")
            Response.error(response.code(), error)
        }
    }

    companion object {
        fun koinModule(): Module {
            return module {
                includes(serviceModule)
                single {
                    AuthRepository(
                        provider = AuthServiceProvider()
                    )
                }
            }
        }
    }
}