package com.ajkerdeal.app.ajkerdealadmin.ui.attendance_report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.report.WorkReportData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewAttendanceReportListBinding

class AttendanceReportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<WorkReportData> = mutableListOf()
    var onItemClick: ((dataList: WorkReportData, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewAttendanceReportListBinding = ItemViewAttendanceReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.employeeName.text = if (model.userName.isNullOrEmpty()){
                "Employee"
            }else{
                model.userName
            }
            binding.employeeId.text = "(${model.userid})"
            binding.workPlace.text = if (model.from.isNullOrEmpty()) "-" else model.from
            binding.workHour.text = model.workingTime
            binding.breakHour.text = model.breakTime
            if (model.session?.isNullOrEmpty() == false) {
                val firstSession = model.session?.first()
                val lastSession = model.session?.last()
                binding.startWorkTime.text = if (firstSession!!.startTime.isNullOrEmpty()) "-" else firstSession.startTime
                binding.endWorkTime.text = if (lastSession!!.endTime.isNullOrEmpty()) "-" else lastSession.endTime
                binding.workSummary.text = if (firstSession.work.isNullOrEmpty()) "-" else firstSession.work
            } else {
                binding.startWorkTime.text = "-"
                binding.endWorkTime.text = "-"
                binding.workSummary.text = "-"
            }
        }
    }

    internal inner class ViewModel(val binding: ItemViewAttendanceReportListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<WorkReportData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<WorkReportData>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}