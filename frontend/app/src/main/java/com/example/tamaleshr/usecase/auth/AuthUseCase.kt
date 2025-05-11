package com.example.tamaleshr.usecase.auth

import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.auth.AuthRepository
import com.example.tamaleshr.service.auth.AuthResponse
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCase
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.defaultFailureFlow
import com.example.tamaleshr.usecase.successFlow
import com.example.tamaleshr.util.AuthTokenManager
import kotlinx.coroutines.flow.Flow

class AuthUseCase(
    private val username: String,
    private val password: String,
    private val clientId: String = "tamalesHr-rest-api",
    private val grantType: String = "password",
    private val tokenManager: AuthTokenManager,
    override val repository: AuthRepository,
): UseCase<AuthResponse, AuthRepository> {

    override suspend fun launch(): Flow<UseCaseResult<AuthResponse, DefaultError>> {
        return try {
            val response = repository.authToken(
                username = username,
                password = password,
                clientId = clientId,
                grantType = grantType
            )
            val authResponse = response.body()
            val errorResponse = response.errorBody()
            when{
                authResponse != null -> {
                    tokenManager.saveUsername(username)
                    tokenManager.saveTokens(authResponse)
                    successFlow(authResponse)
                }
                errorResponse != null -> defaultFailureFlow(code = response.code(), errorResponse)
                else -> defaultFailureFlow(response)
            }
        }catch(e: Exception){
            defaultFailureFlow(e)
        }
    }
}