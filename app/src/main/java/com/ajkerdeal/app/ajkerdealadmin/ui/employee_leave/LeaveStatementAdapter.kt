package com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveListsItem
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLeaveListsBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager

class LeaveStatementAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<LeaveListsItem> = mutableListOf()
    var onApprovedClick: ((dataList: LeaveListsItem, status: String, position: Int) -> Unit)? = null
    var onRejectedClick: ((dataList: LeaveListsItem, status: String, position: Int) -> Unit)? = null

    var onRecommendClick: ((dataList: LeaveListsItem, position: Int) -> Unit)? = null
    var onRecommendRejectedClick: ((dataList: LeaveListsItem, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewLeaveListsBinding = ItemViewLeaveListsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            if (SessionManager.role == "Managing Director" && model.status == "Pending" && model.recommended) {
                binding.leaveActionLayout.visibility  = View.VISIBLE
            }else{
                binding.leaveActionLayout.visibility  = View.GONE
            }

            if (SessionManager.role == "General Manager" && model.status == "Pending" && !model.recommended) {
                binding.recommendActionLayout.visibility  = View.VISIBLE
            }else{
                binding.recommendActionLayout.visibility  = View.GONE
            }

            when (model.status) {
                "Pending" -> {
                    binding.leaveStatus.setTextColor(Color.BLUE)
                }
                "Approved" -> {
                    binding.leaveStatus.setTextColor(Color.parseColor("#056608"))
                }
                else -> {
                    binding.leaveStatus.setTextColor(Color.RED)
                }
            }

            binding.employeeName.text = if (model.name!!.isEmpty()){
                "Employee"
            }else{
                model.name!!
            }
            binding.leaveDate.text = model.date
            binding.department.text = model.department
            binding.leaveStatus.text = model.status
            binding.joiningDate.text = model.resumptionDate
            binding.leaveType.text = model.leaveType
            binding.leaveReason.text = model.reason
            binding.leaveDuration.text = "${model.leaveStart} - ${model.leaveEnd}"
        }
    }

    @SuppressLint("SetTextI18n")
    internal inner class ViewModel(val binding: ItemViewLeaveListsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.approveBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onApprovedClick?.invoke(dataList[absoluteAdapterPosition], "Approved",absoluteAdapterPosition)

                }
            }
        }
        init {
            binding.rejectBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onRejectedClick?.invoke(dataList[absoluteAdapterPosition], "Rejected",absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.recommendBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onRecommendClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)

                }
            }
        }
        init {
            binding.recommendRejectBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onRecommendRejectedClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<LeaveListsItem>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(index: Int){
        dataList.removeAt(index)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<LeaveListsItem>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}