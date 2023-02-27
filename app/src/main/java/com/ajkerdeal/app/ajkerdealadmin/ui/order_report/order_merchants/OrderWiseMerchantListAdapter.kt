package com.ajkerdeal.app.ajkerdealadmin.ui.order_report.order_merchants

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.report.WorkReportData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewOrderWiseMerchantListBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager

class OrderWiseMerchantListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<WorkReportData> = mutableListOf()
    var onItemClick: ((dataList: WorkReportData, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewOrderWiseMerchantListBinding = ItemViewOrderWiseMerchantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.merchantName.text= model.userName
            binding.merchantId.text= model.userid.toString()
            binding.totalOrder.text = "70"
            binding.address.text = model.from
            binding.mobile.text = SessionManager.mobile

        }
    }

    internal inner class ViewModel(val binding: ItemViewOrderWiseMerchantListBinding) : RecyclerView.ViewHolder(binding.root) {
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