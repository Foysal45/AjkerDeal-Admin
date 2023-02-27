package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.location_selection_bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.profile_informations.DistrictModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLocationBinding



class LocationSelectionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<DistrictModel> = mutableListOf()
    var onItemClicked: ((position: Int, value: DistrictModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemViewLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding

            binding.locationName.text = model.districtBng
            binding.locationNameEng.text = model.district

//            val backgroundColor = if (model.isDeactivate)
//                ContextCompat.getColor(binding.parent.context, R.color.black_15)
//            else ContextCompat.getColor(binding.parent.context, R.color.white)
//            binding.parent.setBackgroundColor(backgroundColor)
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

    fun setDataList(list: List<DistrictModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }
}