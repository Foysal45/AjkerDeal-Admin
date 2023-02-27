package com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.MerchantComplainData
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewMerchantComplainBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class MerchantComplainAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<MerchantComplainData> = mutableListOf()
    var onItemClicked: ((dataList: MerchantComplainData,position: Int) -> Unit)? = null
    var onUpdateBtnClick: ((dataList: MerchantComplainData, position: Int) -> Unit)? = null

    private val charLimit = 85

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewMerchantComplainBinding = ItemViewMerchantComplainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            //model.complain = "The quick brown fox jumps over the lazy dog is an English-language pangram—a sentence that contains all of the letters of the English alphabet."
            if (!model.comments.isNullOrEmpty()) {
                if (!model.isExpand && model.comments!!.length > charLimit) {
                    val subString = model.comments!!.substring(0, charLimit-1) + "...<font color='#00844A'>বিস্তারিত</font>"
                    binding.complainType.text = HtmlCompat.fromHtml(subString, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    binding.complainType.setOnClickListener {
                        if (!model.isExpand) {
                            model.isExpand = true
                            notifyItemChanged(position)
                        }
                    }
                } else {
                    binding.complainType.text = model.comments
                }
            } else {
                binding.complainType.text = ""
            }

            binding.orderCode.text = "DT-${model.orderId}"
            if (model.complaintDate != null) {
                val formattedDate = DigitConverter.toBanglaDate(model.complaintDate!!,"yyyy-MM-dd", false)
                binding.date.text = formattedDate
            } else {
                binding.date.text = ""
            }

            binding.status.text = "স্ট্যাটাস: ${model.currentStatus}"
            if (model.solvedDate != null && model.solvedDate != "0001-01-01T00:00:00Z") {
                val formattedDate = DigitConverter.toBanglaDate(model.solvedDate!!,"yyyy-MM-dd", true)
                binding.status.append(" ($formattedDate)")
            }
        }
    }

    inner class ViewModel(val binding: ItemViewMerchantComplainBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
           binding.root.setOnClickListener {
               if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                   onItemClicked?.invoke(dataList[absoluteAdapterPosition],absoluteAdapterPosition)
               }
           }
        }
        init {
            binding.updateMerchantComplainBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onUpdateBtnClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<MerchantComplainData>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<MerchantComplainData>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }
}