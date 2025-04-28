package com.example.tamaleshr.usecase

import com.example.tamaleshr.ui.BaseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

fun <Model, Error> successFlow(model: Model): Flow<UseCaseResult<Model, Error>> {
    return flowOf(UseCaseResult.Success(model))
}

fun <Model> defaultFailureFlow(
    code: Int,
    errorBody: ResponseBody
): Flow<UseCaseResult<Model, DefaultError>> {
    val message = try {
        JSONObject(errorBody.string()).getString("message")
    } catch (e: JSONException) {
        when (code) {
            401 -> "Unauthorized"
            403 -> "Forbidden"
            else -> "Parse Error"
        }
    }

    return flowOf(
        UseCaseResult.Failure(
            DefaultError(
                message
            )
        )
    )
}

fun <Model> defaultFailureFlow(response: Response<*>): Flow<UseCaseResult<Model, DefaultError>> {
    return when (response.code()) {
        401 -> failureFlow(DefaultError("Parse error"))
        403 -> failureFlow(DefaultError("Parse error"))
        else -> failureFlow(DefaultError("Parse error"))
    }
}

fun <Model> defaultFailureFlow(e: Exception): Flow<UseCaseResult<Model, DefaultError>> {
    return failureFlow(DefaultError(e.message ?: "Unknown Error"))
}

fun <Model, Error> UseCaseResult<Model, Error>.failure(): BaseUiState<Model, Error> {
    val error = error ?: throw IllegalArgumentException("Error is not provided in error event")
    return BaseUiState.Error(error)
}

fun  <Model, Error> UseCaseResult<Model, Error>.success(): BaseUiState<Model, Error> {
    val data = data ?: throw IllegalArgumentException("Data is not provided in success event")
    return BaseUiState.Success(data)
}

fun <Model, Error> failureFlow(error: Error) = flowOf(UseCaseResult.Failure<Model, Error>(error))