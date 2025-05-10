package com.example.tamaleshr.ui.salary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tamaleshr.databinding.FragmentSalaryBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tamaleshr.ui.salary.SalaryAdapter

class SalaryFragment : Fragment() {

    private var _binding: FragmentSalaryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSalaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup RecyclerView
        val adapter = SalaryAdapter()
        binding.salaryList.layoutManager = LinearLayoutManager(requireContext())
        binding.salaryList.adapter = adapter

        val viewModel = ViewModelProvider(this).get(SalaryViewModel::class.java)
        val employeeId = arguments?.getString("empId") ?: "10004"
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state is SalaryUiState.Success) {
                adapter.submitList(state.salaries)
            }
        }
        // Trigger data fetch
        viewModel.fetchSalary(employeeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}