package com.example.tamaleshr.ui

sealed class BaseUiState<Model, Error>(val data: Model?, val error: Error?) {
    class Loading<Model, Error> : BaseUiState<Model, Error>(null, null)
    class Success<Model, Error>(data: Model) : BaseUiState<Model, Error>(data, null)
    class Error<Model, Error>(error: Error) : BaseUiState<Model, Error>(null, error)
}