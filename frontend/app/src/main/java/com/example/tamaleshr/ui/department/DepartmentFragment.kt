package com.example.tamaleshr.ui.department

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.DialogAddEmployeeBinding
import com.example.tamaleshr.databinding.FragmentDepartmentBinding
import com.example.tamaleshr.databinding.FragmentHomeBinding
import com.example.tamaleshr.service.department.Department
import com.example.tamaleshr.service.department.DepartmentEmployee
import com.example.tamaleshr.ui.BaseUiState
import com.example.tamaleshr.usecase.DefaultError
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DepartmentFragment : Fragment() {
    private val viewModel by viewModels<DepartmentViewModel> { DepartmentViewModel.Factory }
    private var _binding: FragmentDepartmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepartmentBinding.inflate(inflater, container, false)
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun onTextChanged(username: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (username.toString().isBlank()) {
                    viewModel.filterUsers("") // reset entire list
                }
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = binding.etSearch.text.toString()
                viewModel.filterUsers(query)
                true // handled
            } else {
                false // not handled
            }
        }
        binding.fabAddEmployee.setOnClickListener {
            showAddEmployeeDialog {
                viewModel.addUser(it) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.user_was_added_successfully), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.rvEmployees.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEmployees.adapter = DepartmentEmployeeAdapter {
            showRemoveConfirmationDialog(it) {
                viewModel.removeUser(it.employee.employeeNo) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.user_was_removed_successfully), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uiResultLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BaseUiState.Error<DepartmentViewModel.DepartmentUiData, DefaultError> -> {
                    binding.progressBar.isVisible = false
                    binding.tvTitle.visibility = View.VISIBLE
                    binding.etSearch.visibility = View.VISIBLE
                    binding.fabAddEmployee.isEnabled = true
                    binding.rvEmployees.visibility = View.VISIBLE
                    binding.tvEmpty.visibility = View.GONE
                }

                is BaseUiState.Loading<DepartmentViewModel.DepartmentUiData, DefaultError> -> {
                    binding.progressBar.isVisible = true
                    binding.tvTitle.visibility = View.INVISIBLE
                    binding.etSearch.visibility = View.INVISIBLE
                    binding.rvEmployees.visibility = View.INVISIBLE
                    binding.tvEmpty.visibility = View.GONE
                    binding.fabAddEmployee.isEnabled = false
                }

                is BaseUiState.Success<DepartmentViewModel.DepartmentUiData, DefaultError> -> {
                    binding.progressBar.isVisible = false
                    binding.tvTitle.visibility = View.VISIBLE
                    binding.etSearch.visibility = View.VISIBLE
                    binding.fabAddEmployee.isEnabled = true
                    binding.rvEmployees.visibility = View.VISIBLE
                    binding.tvEmpty.isVisible = state.data?.filteredUsers.orEmpty().isEmpty()
                    binding.tvTitle.text = buildString {
                        append(state.data?.data?.departmentInfo?.departmentName)
                        append(" ")
                        append(getString(R.string.department))
                    }
                    (binding.rvEmployees.adapter as? DepartmentEmployeeAdapter)?.submitList(state.data?.filteredUsers)
                }
            }
        }
        viewModel.fetchDepartmentData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showAddEmployeeDialog(onSubmit: (Int) -> Unit) {
        val binding = DialogAddEmployeeBinding.inflate(LayoutInflater.from(requireContext()))

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_employee_title))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.add)) { dialog, _ ->
                val inputText = binding.etEmpNo.text.toString()
                if (inputText.isNotBlank()) {
                    val empNo = inputText.toIntOrNull()
                    if (empNo != null) {
                        onSubmit(empNo)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.invalid_employee_number), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showRemoveConfirmationDialog(emp: DepartmentEmployee, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.remove_employee_title))
            .setMessage(
                getString(
                    R.string.are_you_sure_you_want_to_remove,
                    emp.employee.employeeNo.toString()
                )
            )
            .setPositiveButton(getString(R.string.remove)) { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel_remove)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}