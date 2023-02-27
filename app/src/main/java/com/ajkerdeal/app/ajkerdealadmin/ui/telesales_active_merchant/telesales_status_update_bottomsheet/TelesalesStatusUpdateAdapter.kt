package com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant.telesales_status_update_bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewTelesalesStatusUpdateBinding

class TelesalesStatusUpdateAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<String> = mutableListOf()

    var onItemClicked: ((model: String, position: Int) -> Unit)? = null

    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ViewHolder(ItemViewTelesalesStatusUpdateBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding

            binding.title.text = model
            val background = if (selectedPosition == position) {
                ContextCompat.getDrawable(binding.title.context, R.drawable.bg_telesales_option_selected)
            } else {
                ContextCompat.getDrawable(binding.title.context, R.drawable.bg_telesales_option_unselected)
            }
            binding.title.background = background

            val color = if (selectedPosition == position) {
                ContextCompat.getColor(binding.title.context, R.color.white)
            } else {
                ContextCompat.getColor(binding.title.context, R.color.black_80)
            }
            binding.title.setTextColor(color)
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(val binding: ItemViewTelesalesStatusUpdateBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClicked?.invoke(dataList[position], position)
                    selectedPosition = position
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun initLoad(list: List<String>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }


}