package com.example.tamaleshr.ui.department

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DepartmentViewModel(
    private val dispatcher: CoroutineDispatcher
): ViewModel() {



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DepartmentViewModel(Dispatchers.IO)
            }
        }
    }
}