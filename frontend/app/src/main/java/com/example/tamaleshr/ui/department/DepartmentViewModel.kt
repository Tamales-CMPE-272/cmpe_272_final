package com.example.tamaleshr.ui.department

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tamaleshr.di.DispatcherProvider
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.department.Department
import com.example.tamaleshr.service.department.DepartmentEmployee
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.usecase.department.AddUserUseCase
import com.example.tamaleshr.usecase.department.DepartmentUseCase
import com.example.tamaleshr.usecase.department.RemoveUserUseCase
import com.example.tamaleshr.usecase.failure
import com.example.tamaleshr.usecase.success
import com.example.tamaleshr.usecase.successFlow
import com.example.tamaleshr.util.AuthTokenManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DepartmentViewModel() : ViewModel() {

    private val _uiResultLiveData = MutableLiveData<BaseUiState<DepartmentUiData, DefaultError>>()
    val uiResultLiveData: LiveData<BaseUiState<DepartmentUiData, DefaultError>>
        get() = _uiResultLiveData
    private val authTokenManager: AuthTokenManager
        get() = koin.get<AuthTokenManager>()

    private val dispatchers: DispatcherProvider = koin.get()

    fun filterUsers(text: String) {
        val newEmp = _uiResultLiveData.value?.data?.data?.departmentEmployees.orEmpty().filter {
            text.isBlank() || it.employee.employeeNo.toString().contains(text)
        }
        val data = _uiResultLiveData.value?.data?.data ?: return
        val newData = DepartmentUiData(
            filteredUsers = newEmp,
            data = data
        )
        viewModelScope.launch(dispatchers.main){
            _uiResultLiveData.value = BaseUiState.Success(newData)
        }
    }

    fun removeUser(empNo: Int, callback: () -> Unit){
        val deptId = authTokenManager.deptId() ?: return
        _uiResultLiveData.postValue(BaseUiState.Loading())
        viewModelScope.launch(dispatchers.io) {
            RemoveUserUseCase(
                departmentId = deptId,
                empId = empNo,
                repository = koin.get()
            ).launch().collectLatest { result ->
                when (result) {
                    is UseCaseResult.Failure<Department, DefaultError> -> {
                        val error = result.error ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(BaseUiState.Error(error))
                        }
                    }

                    is UseCaseResult.Success<Department, DefaultError> -> {
                        val data = result.data ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            callback.invoke()
                            _uiResultLiveData.postValue(
                                BaseUiState.Success(
                                    DepartmentUiData(
                                        filteredUsers = data.departmentEmployees,
                                        data = data
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun addUser(empNo: Int, callback: () -> Unit){
        val deptId = authTokenManager.deptId() ?: return
        _uiResultLiveData.postValue(BaseUiState.Loading())
        viewModelScope.launch(dispatchers.io) {
            AddUserUseCase(
                departmentId = deptId,
                empId = empNo,
                repository = koin.get()
            ).launch().collectLatest { result ->
                when (result) {
                    is UseCaseResult.Failure<Department, DefaultError> -> {
                        val error = result.error ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(BaseUiState.Error(error))
                        }
                    }

                    is UseCaseResult.Success<Department, DefaultError> -> {
                        val data = result.data ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            callback.invoke()
                            _uiResultLiveData.postValue(
                                BaseUiState.Success(
                                    DepartmentUiData(
                                        filteredUsers = data.departmentEmployees,
                                        data = data
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    fun fetchDepartmentData() {
        val deptId = authTokenManager.deptId() ?: return
        _uiResultLiveData.postValue(BaseUiState.Loading())
        viewModelScope.launch(dispatchers.io) {
            DepartmentUseCase(
                departmentId = deptId,
                repository = koin.get()
            ).launch().collectLatest { result ->
                when (result) {
                    is UseCaseResult.Failure<Department, DefaultError> -> {
                        val error = result.error ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(BaseUiState.Error(error))
                        }
                    }

                    is UseCaseResult.Success<Department, DefaultError> -> {
                        val data = result.data ?: return@collectLatest
                        viewModelScope.launch(dispatchers.main) {
                            _uiResultLiveData.postValue(
                                BaseUiState.Success(
                                    DepartmentUiData(
                                        filteredUsers = data.departmentEmployees,
                                        data = data
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    data class DepartmentUiData(
        val filteredUsers: List<DepartmentEmployee>,
        val data: Department
    )


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DepartmentViewModel()
            }
        }
    }
}