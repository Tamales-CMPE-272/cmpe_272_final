package com.example.tamaleshr.ui.department

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.ItemDepartmentEmployeeBinding
import com.example.tamaleshr.service.department.DepartmentEmployee
import com.example.tamaleshr.service.department.DepartmentEmployeeData

class DepartmentEmployeeAdapter(
    private val onRemoveClicked: (DepartmentEmployee) -> Unit
) : ListAdapter<DepartmentEmployee, DepartmentEmployeeAdapter.EmployeeViewHolder>(DiffCallback) {

    class EmployeeViewHolder(
        val binding: ItemDepartmentEmployeeBinding,
        private val onRemoveClicked: (DepartmentEmployee) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: DepartmentEmployee) {
            // Initials
            val initials = "${employee.employee.firsName.firstOrNull() ?: ""}${employee.employee.lastName.firstOrNull() ?: ""}".uppercase()
            binding.tvInitials.text = initials

            // Full Name (using string resource)
            binding.tvFullName.text = binding.root.context.getString(
                R.string.full_name_placeholder,
                employee.employee.firsName,
                employee.employee.lastName
            )

            // Emp No (using string resource)
            binding.tvEmpNo.text = binding.root.context.getString(
                R.string.home_emp_no,
                employee.employee.employeeNo.toString()
            )

            // Remove Button Click
            binding.btnRemove.setOnClickListener {
                onRemoveClicked(employee)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemDepartmentEmployeeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding, onRemoveClicked)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DepartmentEmployee>() {
        override fun areItemsTheSame(oldItem: DepartmentEmployee, newItem: DepartmentEmployee): Boolean {
            // Assuming emp_no uniquely identifies an employee
            return oldItem.employee.employeeNo == newItem.employee.employeeNo
        }

        override fun areContentsTheSame(oldItem: DepartmentEmployee, newItem: DepartmentEmployee): Boolean {
            return oldItem == newItem
        }
    }
}