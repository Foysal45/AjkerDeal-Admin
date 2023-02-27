package com.ajkerdeal.app.ajkerdealadmin.ui.operation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.barcode.BarcodeInfo
import com.ajkerdeal.app.ajkerdealadmin.api.models.leave.LeaveListsItem
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewBarcodeInfoBinding

class BarcodeInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<BarcodeInfo> = mutableListOf()

    var onDelete: ((model: BarcodeInfo, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewBarcodeInfoBinding = ItemViewBarcodeInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.barcodeData.text = model.data
        }
    }

    inner class ViewModel(val binding: ItemViewBarcodeInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.deleteBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onDelete?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<BarcodeInfo>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(mode: BarcodeInfo) {
        dataList.add(mode)
        notifyItemInserted(dataList.size)
    }

    fun addUniqueData(mode: BarcodeInfo): Boolean {
        val found = dataList.find { it.data == mode.data }
        return if (found == null) {
            dataList.add(mode)
            notifyItemInserted(dataList.size)
            true
        } else false
    }

    fun removeData(mode: BarcodeInfo, position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun dataList() = dataList

}