package com.ajkerdeal.app.ajkerdealadmin.ui.post_shipment_chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUser
import com.ajkerdeal.app.ajkerdealadmin.api.models.courier_users_info.CourierUsersInfoResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewChatForPostShipmentBinding
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

class PostShipmentChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<CourierUser> = mutableListOf()
    var onItemClicked: ((model: CourierUser) -> Unit)? = null

    private val sdf = SimpleDateFormat("dd MMM, yy", Locale.US)

    private var opitions = RequestOptions().placeholder(R.drawable.ic_person_circle).error(R.drawable.ic_person_circle).circleCrop()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemViewChatForPostShipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    inner class ViewModel(val binding: ItemViewChatForPostShipmentBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                if(absoluteAdapterPosition != RecyclerView.NO_POSITION){
                    onItemClicked?.invoke(dataList[absoluteAdapterPosition])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val modelData = dataList[position]
            val binding = holder.binding
            binding.userName.text = if(modelData.companyName?.trim().isNullOrEmpty()) "Merchant Name" else modelData.companyName
            //binding.userName.text = modelData.accountName!!
            binding.mobileNumber.text = modelData.mobile!!

        }
    }

    override fun getItemCount(): Int = dataList.size

    fun initLoad(list: List<CourierUser>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<CourierUser>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount ?: 0)
    }

}