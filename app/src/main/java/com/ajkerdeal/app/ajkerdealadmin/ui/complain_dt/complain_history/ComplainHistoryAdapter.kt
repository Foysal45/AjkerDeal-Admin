package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.ComplainHistoryData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainHistoryBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class ComplainHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<ComplainHistoryData> = mutableListOf()
    var onItemClick: ((dataList: ComplainHistoryData, position: Int) -> Unit)? = null

    var complainDate: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewComplainHistoryBinding = ItemViewComplainHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            val date = if (model.updatedOn?.contains("Monday, 01 January 0001", true) == true) {
                DigitConverter.formatDate(complainDate, "yyyy-MM-dd'T'HH:mm", "dd MMM yyyy hh:mm a")
            } else {
                model.updatedOn
            }
            binding.date.text = date
            binding.comment.text = model.comments
            binding.commentedBy.text = ("Commented By : ${model.commentedBy}")

        }
    }

    internal inner class ViewModel(val binding: ItemViewComplainHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
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