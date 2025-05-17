package com.example.tamaleshr.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.profile.Profile
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.ui.MainViewModel
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.profile.ProfileUseCase
import com.example.tamaleshr.usecase.failure
import com.example.tamaleshr.usecase.success
import com.example.tamaleshr.util.AuthTokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel() : ViewModel() {

    private val _uiResultLiveData = MutableLiveData<BaseUiState<Profile, DefaultError>>()
    val uiResultLiveData: LiveData<BaseUiState<Profile, DefaultError>>
        get() = _uiResultLiveData

    private val dispatchers: DispatcherProvider = koin.get()

    fun fetchProfile(employeeId: String = koin.get<AuthTokenManager>().getUsername()){
        _uiResultLiveData.value = BaseUiState.Loading()
        viewModelScope.launch(dispatchers.io) {
            ProfileUseCase(
                employeeId = employeeId,
                repository = koin.get()
            ).launch().collectLatest { result ->
                when (result) {
                    is UseCaseResult.Failure<Profile, DefaultError> -> {
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(result.failure())
                        }
                    }
                    is UseCaseResult.Success<Profile, DefaultError> -> {
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
                ProfileViewModel()
            }
        }
    }
}