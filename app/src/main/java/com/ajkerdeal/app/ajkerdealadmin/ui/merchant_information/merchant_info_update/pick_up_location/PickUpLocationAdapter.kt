package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.pick_up_location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewPickupAddressBinding

class PickUpLocationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataList: MutableList<MerchantPickupLocation> = mutableListOf()
    var onItemClicked: ((model: MerchantPickupLocation) -> Unit)? = null
    var onEditClicked: ((model: MerchantPickupLocation) -> Unit)? = null
    var onDeleteClicked: ((model: MerchantPickupLocation) -> Unit)? = null
    var showCount: Boolean = false
    var showEdit: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemViewPickupAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            if (model.id == 0) {
                binding.thana.text = "${model.thanaName}"
                binding.address.text = model.pickupAddress

            } else {
                binding.thana.text = "থানা: ${model.thanaName}"
                binding.address.text = "পিকআপ ঠিকানা: ${model.pickupAddress}"
            }
            if (showCount) {
                binding.orderCount.visibility = View.VISIBLE
            } else {
                binding.orderCount.visibility = View.GONE
            }

            if (showEdit) {
                binding.editBtn.visibility = View.VISIBLE
                binding.deleteBtn.visibility = View.VISIBLE
            } else {
                binding.editBtn.visibility = View.GONE
                binding.deleteBtn.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewModel(val binding: ItemViewPickupAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.editBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onEditClicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
            binding.deleteBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onDeleteClicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }

    fun initList(list: List<MerchantPickupLocation>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun   addItem(model: MerchantPickupLocation) {
        dataList.add(model)
        notifyItemInserted(dataList.lastIndex)

    }

}