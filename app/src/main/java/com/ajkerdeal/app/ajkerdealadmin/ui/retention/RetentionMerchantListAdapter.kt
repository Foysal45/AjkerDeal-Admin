package com.ajkerdeal.app.ajkerdealadmin.ui.retention

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewRetentionWiseMerchantListsBinding
import java.text.SimpleDateFormat
import java.util.*

class RetentionMerchantListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<RetentionMerchentListModel> = mutableListOf()

    var onCallInfoClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onVisitInfoClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onComplainAddClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onLoanApplyClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onQuickBookingClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onCallClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    var onReturnClicked: ((model: RetentionMerchentListModel, position: Int) -> Unit)? = null
    //var getReturnTv: ((model: RetentionMerchentListModel, returnCount: TextView) -> Unit)? = null

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewRetentionWiseMerchantListsBinding = ItemViewRetentionWiseMerchantListsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.merchantName.text =  if (model.companyName.isNullOrEmpty()){
                "Merchant"
            }else{
                model.companyName
            }
            binding.merchantId.text = "Id: ${model.courierUserId}"
            binding.totalOrder.text = model.totalOrder.toString()
            binding.lastOrderDate.text =  if (model.lastOrderDate.isNullOrEmpty()){
                "--"
            }else{
                model!!.lastOrderDate!!
            }
            binding.mobileNumber.text =  if (model.mobile.isNullOrEmpty()){
                " "
            }else{
                model.mobile
            }

           /* if (!model.alterMobile.isNullOrEmpty()){
                binding.mobileNumber.append("\n${model.alterMobile}")
            }*/
            binding.address.text = model.address

            //getReturnTv?.invoke(model, binding.returnCountBtn)
        }
    }

    internal inner class ViewModel(val binding: ItemViewRetentionWiseMerchantListsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.callInfoBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onCallInfoClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.visitInfoBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onVisitInfoClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.complainBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onComplainAddClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.quickBookingBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onQuickBookingClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.mobileNumberLayout.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onCallClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
        init {
            binding.returnCountBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onReturnClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }

        init {
            binding.applyLoanTV.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onLoanApplyClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

    fun initLoad(list: List<RetentionMerchentListModel>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<RetentionMerchentListModel>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

}