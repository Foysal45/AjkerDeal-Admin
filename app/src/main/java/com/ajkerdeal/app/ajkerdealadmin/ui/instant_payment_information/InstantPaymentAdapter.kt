package com.ajkerdeal.app.ajkerdealadmin.ui.instant_payment_information

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewInstantPaymentInformationBinding
import com.ajkerdeal.app.ajkerdealadmin.api.models.instant_payment_info.InstantPaymentModel
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class InstantPaymentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<InstantPaymentModel> = mutableListOf()

    var onItemClicked: ((modelInstantPayment: InstantPaymentModel, position: Int) -> Unit)? = null
    var onMobileNumberClick: ((modelInstantPayment: InstantPaymentModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewInstantPaymentInformationBinding = ItemViewInstantPaymentInformationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding
            binding?.companyNameInstantPayment?.text = if (model.companyName.isNotEmpty()){
                model.companyName
            }else{
                "Company Name"
            }
            binding?.mobileTelesale?.text = model.mobile
            binding?.bkashNumberInstantPayment?.text = model.bkashNumber
            binding?.emailAddressInstantPayment?.text = model.emailAddress
            binding?.isActiveInstantPayment?.text = if (model.isActive){"Yes"}else{"No"}
        }
    }
    override fun getItemCount(): Int = dataList.size

    internal inner class ViewModel(val binding: ItemViewInstantPaymentInformationBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mobileNumberLayout.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onMobileNumberClick?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }


    fun initLoad(list: List<InstantPaymentModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    /*fun pagingLoad(list: List<InstantPaymentModel>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }*/
}