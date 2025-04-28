package com.example.tamaleshr.ui.salary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SalaryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is salary Fragment"
    }
    val text: LiveData<String> = _text
}