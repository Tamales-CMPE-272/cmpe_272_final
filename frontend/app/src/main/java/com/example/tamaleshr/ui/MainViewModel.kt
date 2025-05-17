package com.example.tamaleshr.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.employee.EmployeeUseCase
import com.example.tamaleshr.usecase.failure
import com.example.tamaleshr.usecase.success
import com.example.tamaleshr.util.AuthTokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

    private val _uiResultLiveData = MutableLiveData<BaseUiState<Employee, DefaultError>>()
    val uiResultLiveData: LiveData<BaseUiState<Employee, DefaultError>>
        get() = _uiResultLiveData
    private val authTokenManager: AuthTokenManager
        get() = koin.get<AuthTokenManager>()

    private val dispatchers: DispatcherProvider = koin.get()

    fun fetchEmployee(employeeId: String = koin.get<AuthTokenManager>().getUsername()){
        _uiResultLiveData.value = BaseUiState.Loading()
        viewModelScope.launch(dispatchers.io) {
            EmployeeUseCase(
                employeeId = employeeId,
                repository = koin.get()
            ).launch().collectLatest { result ->
                when (result) {
                    is UseCaseResult.Failure<Employee, DefaultError> -> {
                       viewModelScope.launch(dispatchers.main) {
                           _uiResultLiveData.postValue(result.failure())
                       }
                    }
                    is UseCaseResult.Success<Employee, DefaultError> -> {
                        authTokenManager.saveDeptId(result.data?.managerData)
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(result.success())
                        }
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel()
            }
        }
    }
}