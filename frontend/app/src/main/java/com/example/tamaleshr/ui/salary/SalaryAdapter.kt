package com.example.tamaleshr.ui.salary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tamaleshr.R
import com.example.tamaleshr.service.salary.Salary

/**
 * Adapter to show a list of Salary entries in a RecyclerView.
 */
class SalaryAdapter :
    ListAdapter<Salary, SalaryAdapter.ViewHolder>(SalaryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val empNo: TextView     = itemView.findViewById(R.id.empNo)
        private val fromDate: TextView  = itemView.findViewById(R.id.fromDate)
        private val toDate: TextView    = itemView.findViewById(R.id.toDate)
        private val salary: TextView    = itemView.findViewById(R.id.salary)

        fun bind(item: Salary) {
            empNo.text    = item.empNo?.toString() ?: "-"
            fromDate.text = item.fromDate?.toString() ?: "-"
            toDate.text   = item.toDate?.toString() ?: "-"
            salary.text   = item.salary?.toString() ?: "-"
        }
    }

    class SalaryDiffCallback : DiffUtil.ItemCallback<Salary>() {
        override fun areItemsTheSame(old: Salary, new: Salary): Boolean {
            // Treat each empNo+fromDate as unique key
            return old.empNo == new.empNo && old.fromDate == new.fromDate
        }

        override fun areContentsTheSame(old: Salary, new: Salary): Boolean {
            return old == new
        }
    }
}