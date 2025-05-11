package com.example.tamaleshr.ui.department

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.tamaleshr.databinding.FragmentDepartmentBinding
import com.example.tamaleshr.databinding.FragmentHomeBinding

class DepartmentFragment: Fragment() {
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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}