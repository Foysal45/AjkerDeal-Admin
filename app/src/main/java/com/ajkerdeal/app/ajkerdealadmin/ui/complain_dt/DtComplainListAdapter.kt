package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainData
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave_report.LeaveDataDetails
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainListBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLeaveReportDetailsListBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter
import java.text.SimpleDateFormat
import java.util.*

class DtComplainListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<ComplainData> = mutableListOf()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var onItemClick: ((dataList: ComplainData, position: Int) -> Unit)? = null
    var onUpdateBtnClick: ((dataList: ComplainData, position: Int) -> Unit)? = null

    var complainStatus: String = ""
    private var flagIsIssueSolved = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewComplainListBinding = ItemViewComplainListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.orderCode.text = "DT-${model.orderId}"
            if (model.complaintDate != null) {
                val formattedDate = DigitConverter.formatDate(model.complaintDate, "yyyy-MM-dd", "yyyy-MM-dd")
                binding.date.text = formattedDate
            } else {
                binding.date.text = ""
            }

            binding.assignedTo.text = "(${model.assignedTo})"

            binding.complainType.text = model.comments

            binding.status.text = "Status: $complainStatus"
            if (model.solvedDate != null && model.solvedDate != "0001-01-01T00:00:00Z") {
                val formattedDate = DigitConverter.formatDate(model.solvedDate, "yyyy-MM-dd", "dd MMM, yyyy")
                binding.status.append(" ($formattedDate)")
            }

            binding.userName.text = "Complain by: ${model.fullName}"
            if(flagIsIssueSolved == 9) binding.updateDTComplainBtn.visibility = View.VISIBLE
            else if (flagIsIssueSolved == 0) binding.updateDTComplainBtn.visibility = View.VISIBLE
        }
    }

    internal inner class ViewModel(val binding: ItemViewComplainListBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.updateDTComplainBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onUpdateBtnClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<ComplainData>, status: String, flag: Int) {
        dataList.clear()
        dataList.addAll(list)
        complainStatus = status
        flagIsIssueSolved = flag
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<ComplainData>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}