package com.example.tamaleshr.usecase

import kotlinx.coroutines.flow.Flow

interface UseCase<Model, Repository> {
    val repository: Repository
    suspend fun launch(): Flow<UseCaseResult<Model, DefaultError>>
}