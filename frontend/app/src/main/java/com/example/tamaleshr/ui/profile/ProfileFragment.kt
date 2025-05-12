package com.example.tamaleshr.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tamaleshr.databinding.FragmentProfileBinding
import com.example.tamaleshr.service.profile.Profile
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.usecase.DefaultError
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> { ProfileViewModel.Factory }

    private val dateFormatter = SimpleDateFormat("MM/dd/yy", Locale.US)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.uiResultLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BaseUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is BaseUiState.Error<Profile, DefaultError> -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failed to load profile.", Toast.LENGTH_SHORT).show()
                }
                is BaseUiState.Success<Profile, DefaultError> -> {
                    binding.progressBar.visibility = View.GONE
                    val profile = state.data

                    binding.tvEmpNo.text = "${profile?.emp_no ?: "N/A"}"
                    binding.tvFullName.text = "${profile?.fullName() ?: "N/A"}"
                    binding.tvBirthDate.text = "${formatDate(profile?.birth_date)}"
                    binding.tvHireDate.text = "${formatDate(profile?.hire_date)}"
                    binding.tvTitles.text = "${profile?.titles?.joinToString(", ") ?: "N/A"}"
                    binding.tvDepartments.text = "${profile?.department_names?.joinToString(", ") ?: "N/A"}"
                }
            }
        }

        viewModel.fetchProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(date: java.util.Date?): String {
        return date?.let { dateFormatter.format(it) } ?: "N/A"
    }
}
