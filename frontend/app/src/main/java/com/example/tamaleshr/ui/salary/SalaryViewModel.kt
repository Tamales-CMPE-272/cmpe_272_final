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

class SalaryViewModel(
    private val repository: SalaryRepository = SalaryRepository(SalaryServiceProvider()),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _salaryLiveData = MutableLiveData<List<Salary>?>()
    val salaryLiveData: LiveData<List<Salary>?> = _salaryLiveData

    fun fetchSalary(employeeId: String) {
        viewModelScope.launch(dispatcher) {
            val response = repository.findSalaryByEmployeeId(employeeId)
            if (response != null) {
                if (response.isSuccessful) {
                    _salaryLiveData.postValue(response.body())
                } else {
                    _salaryLiveData.postValue(null)
                }
            }
        }
    }
}