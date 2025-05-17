package com.example.tamaleshr.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.di.DispatcherProviderImpl
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.auth.AuthRepository
import com.example.tamaleshr.service.auth.AuthResponse
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCase
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.auth.AuthUseCase
import com.example.tamaleshr.usecase.failure
import com.example.tamaleshr.usecase.success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AuthViewModel(
): ViewModel() {

    data class Credentials(
        val username: String = "",
        val password: String = ""
    )

    private val dispatchers: DispatcherProvider
        get() = koin.get()

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _credentialsState: MutableLiveData<Credentials> = MutableLiveData(Credentials())
    val credentialsState: LiveData<Credentials>
        get() = _credentialsState


    fun updateUsername(username: String){
        viewModelScope.launch(dispatchers.io) {
            val state = _credentialsState.value ?: Credentials()
            _credentialsState.postValue(
                state.copy(username = username)
            )
        }
    }

    fun updatePassword(password: String){
        viewModelScope.launch(dispatchers.io) {
            val state = _credentialsState.value ?: Credentials()
            _credentialsState.postValue(
                state.copy(password = password)
            )
        }
    }


    fun authenticate(
        callback: (result: UseCaseResult<AuthResponse, DefaultError>) -> Unit
    ) {
        _isLoading.postValue(true)
        viewModelScope.launch(dispatchers.io) {
            AuthUseCase(
                username = credentialsState.value?.username.orEmpty(),
                password = credentialsState.value?.password.orEmpty(),
                tokenManager = koin.get(),
                repository = koin.get()
            ).launch().collectLatest { result ->
                _isLoading.postValue(false)
                viewModelScope.launch(dispatchers.main) {
                    callback.invoke(result)
                }
            }
        }
    }

    companion object {
        val Factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel()
            }
        }
    }
}