package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantDetailsResponse
import com.ajkerdeal.app.ajkerdealadmin.api.models.telesalesactivemerchant.TeleSalesActiveMerchantResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewTelesalesInfoBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class TelesalesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<TeleSalesActiveMerchantResponse> = mutableListOf()

    var onItemClicked: ((modelTelesales: TeleSalesActiveMerchantResponse, position: Int) -> Unit)? = null
    var recentlyInterested: ((modelTelesales: TeleSalesActiveMerchantResponse) -> Unit)? = null
    var notInterested: ((modelTelesales: TeleSalesActiveMerchantResponse) -> Unit)? = null
    var didntPicked: ((modelTelesales: TeleSalesActiveMerchantResponse) -> Unit)? = null
    var busnessClosed: ((modelTelesales: TeleSalesActiveMerchantResponse) -> Unit)? = null
    var latePicked: ((modelTelesales: TeleSalesActiveMerchantResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewTelesalesInfoBinding = ItemViewTelesalesInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding
            binding.dateText.text = model.teleSalesDate
            for (item in model.values) {
                when (item.teleSales) {
                    "Didn't Picked" -> {
                        binding.didntPickedText.visibility = View.VISIBLE
                        binding.didntPicked.visibility = View.VISIBLE
                        binding.didntPicked.text = item.totalCount.toString()
                    }
                    "Recently Interested" -> {
                        binding.recentlyInterestedText.visibility = View.VISIBLE
                        binding.recentlyInterested.visibility = View.VISIBLE
                        binding.recentlyInterested.text = item.totalCount.toString()
                    }
                    "Not Interested" -> {
                        binding.notInterestedText.visibility = View.VISIBLE
                        binding.notInterested.visibility = View.VISIBLE
                        binding.notInterested.text = item.totalCount.toString()
                    }
                    "Business Closed" -> {
                        binding.busnessClosedText.visibility = View.VISIBLE
                        binding.busnessClosed.visibility = View.VISIBLE
                        binding.busnessClosed.text = item.totalCount.toString()
                    }
                    else -> {
                        binding.latePickedText.visibility = View.VISIBLE
                        binding.latePicked.visibility = View.VISIBLE
                        binding.latePicked.text = item.totalCount.toString()
                    }
                }
            }
        }
    }

    internal inner class ViewModel(val binding: ItemViewTelesalesInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            binding.recentlyInterested.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    recentlyInterested?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.notInterested.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    notInterested?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.didntPicked.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    didntPicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.busnessClosed.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    busnessClosed?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.latePicked.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    latePicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }


    fun initLoad(list: List<TeleSalesActiveMerchantResponse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }
}