package com.ajkerdeal.app.ajkerdealadmin.ui.operation.complain_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignedCountDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class ComplainDetailsDataAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataList: MutableList<AutoAssignedCountDetailsResponse> = mutableListOf()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    var onItemClick: ((model: AutoAssignedCountDetailsResponse, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewComplainDetailsBinding = ItemViewComplainDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ComplainDetailsDataAdapter.ViewModel) {
            val model = dataList[position]
            val binding = holder.binding
            binding.typeTV.text = model.complainType
            binding.orderCodeTV.text = "DT-${model.bookingCode}"
            binding.podNumTV.text = model.podNumber
            binding.entryDateTV.text = model.dateOfCall
            binding.orderDateTV.text = model.orderDate
        }
    }

    internal inner class ViewModel(val binding: ItemViewComplainDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }


    fun initLoad(list: List<AutoAssignedCountDetailsResponse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}