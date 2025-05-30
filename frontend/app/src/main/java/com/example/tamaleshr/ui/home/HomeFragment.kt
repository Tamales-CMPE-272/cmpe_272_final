package com.example.tamaleshr.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.FragmentHomeBinding
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.auth.JwtTokenPayload
import com.example.tamaleshr.service.auth.Role
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.ui.MainViewModel
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.util.AuthTokenManager

class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel> { HomeViewModel.Factory }

    private val navController: NavController
        get() = requireView().findNavController()

    @VisibleForTesting
    internal var _binding: FragmentHomeBinding? = null

    private val authTokenManager: AuthTokenManager
        get() = koin.get<AuthTokenManager>()
    private val jwtTokenPayload: JwtTokenPayload?
        get() = authTokenManager.decodeJwtPayloadToModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.clProfile.setOnClickListener {
            navController.navigate(R.id.nav_profile)
        }

        binding.clSalary.setOnClickListener {
            navController.navigate(R.id.nav_salary)
        }

        binding.clManage.setOnClickListener {
            navController.navigate(R.id.nav_department)
        }

        binding.clManage.isVisible = jwtTokenPayload?.role == Role.MANAGER

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiResultLiveData.observe(viewLifecycleOwner){ state ->
            when(state){
                is BaseUiState.Error<Employee, DefaultError> -> Unit // Opt Out
                is BaseUiState.Loading<Employee, DefaultError> -> Unit // Opt Out
                is BaseUiState.Success<Employee, DefaultError> -> {
                    binding.tvTitle.text = requireContext().getString(R.string.home_emp_welcome, state.data?.first_name)
                }
            }
        }
        viewModel.fetchEmployee()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}