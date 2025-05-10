package com.example.tamaleshr.ui.salary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tamaleshr.service.salary.Salary
import com.example.tamaleshr.service.salary.SalaryRepository
import com.example.tamaleshr.service.salary.SalaryServiceProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class SalaryUiState {
    object Idle : SalaryUiState()
    object Loading : SalaryUiState()
    data class Success(val salaries: List<Salary>) : SalaryUiState()
    data class Error(val message: String) : SalaryUiState()
}

class SalaryViewModel(
    private val repository: SalaryRepository = SalaryRepository(SalaryServiceProvider()),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableLiveData<SalaryUiState>(SalaryUiState.Idle)
    val state: LiveData<SalaryUiState> = _state

    fun fetchSalary(employeeId: String) {
        _state.value = SalaryUiState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val response = repository.findSalaryByEmployeeId(employeeId)
                if (response != null && response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _state.postValue(SalaryUiState.Success(body))
                    } else {
                        _state.postValue(SalaryUiState.Error("Empty response body"))
                    }
                } else {
                    val code = response?.code() ?: -1
                    val msg = response?.message() ?: "Unknown error"
                    _state.postValue(SalaryUiState.Error("Error $code: $msg"))
                }
            } catch (e: Exception) {
                _state.postValue(SalaryUiState.Error(e.localizedMessage ?: "Unexpected exception"))
            }
        }
    }
}