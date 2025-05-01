package com.example.tamaleshr.ui.salary

import androidx.lifecycle.ViewModelProvider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tamaleshr.databinding.FragmentSalaryBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tamaleshr.ui.salary.SalaryAdapter

class SalaryFragment : Fragment() {

    private var _binding: FragmentSalaryBinding? = null
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
        val employeeId = arguments?.getString("empId") ?: "12345"
        viewModel.salaryLiveData.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                adapter.submitList(list)
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