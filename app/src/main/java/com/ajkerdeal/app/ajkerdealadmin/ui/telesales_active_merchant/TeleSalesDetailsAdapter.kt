package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewTelesalesDetailsBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class TeleSalesDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<TeleSalesActiveMerchantDetailsResponse> = mutableListOf()

    var onItemClicked: ((modelTelesales: TeleSalesActiveMerchantDetailsResponse) -> Unit)? = null
    var onMobileNumberClick: ((modelTelesales: TeleSalesActiveMerchantDetailsResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewTelesalesDetailsBinding = ItemViewTelesalesDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding
            binding?.nameTelesale?.text = if (model.companyName.isNotEmpty()) {
                model.companyName
            } else {
                "Company Name"
            }
            binding?.mobileTelesale?.text = model.mobile
            binding?.bkashNumber?.text = model.bkashNumber
            binding?.lastOrderDate?.text = DigitConverter.formatDate(model.lastOrderDate.split("T")?.first(), "yyyy-MM-dd", "dd-MM-yyyy")

            if (!model.alterMobile.isNullOrEmpty()) {
                binding?.alterMobile?.visibility = View.VISIBLE
                binding?.key3?.visibility = View.VISIBLE
                binding?.alterMobile?.text = model.alterMobile
            } else {
                binding?.alterMobile?.visibility = View.GONE
                binding?.key3?.visibility = View.GONE
            }
        }
    }

    internal inner class ViewModel(val binding: ItemViewTelesalesDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.mobileTelesale.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onMobileNumberClick?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }


    fun initLoad(list: List<TeleSalesActiveMerchantDetailsResponse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }
}