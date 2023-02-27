package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.retention_acquisition_bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.ad_users.ADUsersModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLocationBinding

class RetentionAcquisitionAdapter  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataList: MutableList<ADUsersModel> = mutableListOf()
    var onItemClicked: ((position: Int, value: ADUsersModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemViewLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding

            binding.locationName.text = model.fullName
        }
    }

    inner class ViewHolder(val binding: ItemViewLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(position, dataList[position])
                }
            }
        }
    }

    fun setDataList(list: List<ADUsersModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}