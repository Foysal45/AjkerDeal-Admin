package com.ajkerdeal.app.ajkerdealadmin.ui.leave_report.report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLeaveReportListBinding

class LeaveReportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<LeaveData> = mutableListOf()
    var onItemClick: ((dataList: LeaveData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewLeaveReportListBinding = ItemViewLeaveReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.employeeName.text =  if (model.name.isNullOrEmpty()){
                "Employee"
            }else{
                model.name
            }
            binding.employeeId.text =  model.userId.toString()
            binding.sickLeave.text = "${model.sickLeave}/14"
            binding.casualLeave.text = "${model.casualLeave}/10"

        }
    }

    internal inner class ViewModel(val binding: ItemViewLeaveReportListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }

    fun initLoad(list: List<LeaveData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<LeaveData>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}