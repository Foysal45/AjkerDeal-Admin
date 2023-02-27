package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_info_update_all_list.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewMarchentInfoBinding

class MerchantInformationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val dataList: MutableList<CourierUser> = mutableListOf()

    var onItemClicked: ((model: CourierUser, position: Int) -> Unit)? = null
    var onMobileNumberClick: ((model: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewMarchentInfoBinding = ItemViewMarchentInfoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MerchantInformationAdapter.ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.merchantName.text = model.companyName
            binding.mobileNumber.text = model.mobile
            binding.documentStatus .text = if(model.isDocument){ "Yes"}else{"No"}
            binding.autoProcessStatus .text = if(model.isAutoProcess){ "Yes"}else{"No"}
            binding.email.text = model.emailAddress
            binding.bkashNumber.text = model.bkashNumber
            binding.address.text = model.address

        }
    }

    override fun getItemCount(): Int = dataList.size


    internal inner class ViewModel(val binding: ItemViewMarchentInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.edit.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            binding.mobileNumber.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onMobileNumberClick?.invoke(dataList[absoluteAdapterPosition].mobile)
                }
            }
        }
    }

    fun initLoad(list: List<CourierUser>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<CourierUser>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}