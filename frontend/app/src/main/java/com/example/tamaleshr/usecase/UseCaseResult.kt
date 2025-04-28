package com.example.tamaleshr.usecase

sealed class UseCaseResult<Model, Error>(val data: Model?, val error: Error?) {
    class Success<Model, Error>(data: Model) : UseCaseResult<Model, Error>(data, null)
    class Failure<Model, Error>(error: Error) : UseCaseResult<Model, Error>(null, error)
}