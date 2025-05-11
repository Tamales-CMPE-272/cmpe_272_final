package com.example.tamaleshr.usecase.profile

import com.example.tamaleshr.service.profile.Profile
import com.example.tamaleshr.service.profile.ProfileRepository
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCase
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.defaultFailureFlow
import com.example.tamaleshr.usecase.successFlow
import kotlinx.coroutines.flow.Flow

class ProfileUseCase(
    private val employeeId: String,
    override val repository: ProfileRepository
) : UseCase<Profile, ProfileRepository> {
    override suspend fun launch(): Flow<UseCaseResult<Profile, DefaultError>> {
        return try {
            val response = repository.fetchProfileById(employeeId)
            val profile = response.body()
            val errorResponse = response.errorBody()
            when {
                profile != null -> successFlow(profile)
                errorResponse != null -> defaultFailureFlow(code = response.code(), errorResponse)
                else -> defaultFailureFlow(response)
            }
        } catch (e: Exception) {
            defaultFailureFlow(e)
        }
    }
}