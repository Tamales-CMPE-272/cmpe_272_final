package com.example.tamaleshr.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.ActivityAuthBinding
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.auth.AuthResponse
import com.example.tamaleshr.ui.MainActivity
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.usecase.UseCaseResult
import com.example.tamaleshr.util.AuthTokenManager

class AuthActivity : AppCompatActivity() {
    private val viewModel by viewModels<AuthViewModel> { AuthViewModel.Factory }
    private lateinit var _binding: ActivityAuthBinding
    val binding: ActivityAuthBinding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAuthBinding.inflate(layoutInflater)
        _binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(username: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updatePassword(username.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })
        _binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(password: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.updateUsername(password.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })
        _binding.btnLogin.setOnClickListener {
            viewModel.authenticate { result ->
                when (result) {
                    is UseCaseResult.Failure<AuthResponse, DefaultError> -> {
                        Toast.makeText(
                            this,
                            R.string.invalid_credentials, Toast.LENGTH_SHORT
                        ).show()
                    }
                    is UseCaseResult.Success<AuthResponse, DefaultError> -> {
                        routeToMain()
                    }
                }
            }
        }
        setContentView(_binding.root)
    }

    override fun onStart() {
        super.onStart()
        if(!koin.get<AuthTokenManager>().getAccessToken().isNullOrBlank() && !koin.get<AuthTokenManager>().getAccessToken().isNullOrBlank()){
            routeToMain()
            return
        }
        viewModel.credentialsState.observe(this) { state ->
            _binding.btnLogin.isEnabled = state.username.isNotBlank() && state.password.isNotBlank()
        }
        viewModel.isLoading.observe(this) { isLoading ->
            _binding.btnLogin.isVisible = !isLoading
            _binding.pbLoading.isVisible = isLoading
        }
    }

    private fun routeToMain() {
        val intent = MainActivity.newIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, AuthActivity::class.java)
        }
    }
}