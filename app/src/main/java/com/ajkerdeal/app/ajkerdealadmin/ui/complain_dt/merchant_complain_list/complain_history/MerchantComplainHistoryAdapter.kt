package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list.complain_history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainHistoryData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewMerchantComplainHistoryBinding

class MerchantComplainHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<ComplainHistoryData> = mutableListOf()
    var onItemClick: ((dataList: ComplainHistoryData, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewMerchantComplainHistoryBinding = ItemViewMerchantComplainHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.date.text = model.updatedOn
            binding.comment.text = model.comments
            binding.commentedBy.text = ("Commented By : ${model.commentedBy}")

        }
    }

    internal inner class ViewModel(val binding: ItemViewMerchantComplainHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<ComplainHistoryData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<ComplainHistoryData>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}