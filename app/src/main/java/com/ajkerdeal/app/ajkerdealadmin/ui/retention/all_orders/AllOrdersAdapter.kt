package com.ajkerdeal.app.ajkerdealadmin.ui.retention.all_orders

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.merchant_order_count.all_orders.CourierOrderViewModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewAllOrderBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter


class AllOrdersAdapter(var context: Context, var dataList: MutableList<CourierOrderViewModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onItemClick: ((model: CourierOrderViewModel, position: Int) -> Unit)? = null
    var onActionClicked: ((model: CourierOrderViewModel, position: Int) -> Unit)? = null
    var onCallClicked: ((model: CourierOrderViewModel, position: Int) -> Unit)? = null
    var isFromDashBoard: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewAllOrderBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_view_all_order, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding

            holder.binding.orderId.text = model?.courierOrdersId.toString()

            val formattedDate = DigitConverter.toBanglaDate(model?.courierOrderDateDetails?.orderDate, "MM-dd-yyyy HH:mm:ss")
            holder.binding.date.text = formattedDate

            holder.binding.reference.text = model?.courierOrderInfo?.collectionName
            holder.binding.customerName.text = model?.customerName
            var mobile = "${model?.courierAddressContactInfo?.mobile}"
            if (model?.courierAddressContactInfo?.otherMobile?.isEmpty() == false) {
                mobile += ",${model?.courierAddressContactInfo?.otherMobile}"
            }
            holder.binding.customerPhone.text = mobile
            //holder.binding.status.text = model?.status
            val status = if (isFromDashBoard) {
                model.dashboardStatusGroup
            } else {
                model.orderTrackStatusGroup
            }
            holder.binding.status.text = status


            if (model.statusId == 60 || model.statusId == 19 || model.statusId == 15) {

                if (model.statusId == 60 || model.statusId == 19) {
                    holder.binding.hubLocationBtn.visibility = View.VISIBLE
                    holder.binding.hubName.text = "${model.hubViewModel?.name}-এ আছে"
                }

                if (model.statusId == 15) {
                    holder.binding.key1.text = "ডেলিভারি তারিখ"
                    val formattedDate1 = DigitConverter.toBanglaDate(model?.courierOrderDateDetails?.updatedOnDate, "MM-dd-yyyy HH:mm:ss")
                    holder.binding.date.text = formattedDate1
                }

            } else {

                holder.binding.key1.text = "তারিখ"

            }

            when (model.statusId) {
                // কাস্টমার প্রোডাক্ট নিতে চায়নি, ক্রেতা ফোনে পাওয়া যায়নি
                26, 33, 47, 27 -> {
                    binding.actionBtn.isVisible = true
                    binding.reattemptStatus.isVisible = false
                    binding.actionBtn.text = "আবার ডেলিভারি এটেম্পট নিন"
                }
                64 -> {
                    binding.reattemptStatus.isVisible = true
                    binding.reattemptStatus.text = "রি-এটেম্পট রিকোয়েস্ট করা হয়েছে"
                }
                else -> {
                    binding.reattemptStatus.isVisible = false
                    binding.actionBtn.isVisible = false
                }
            }

        }

    }

    inner class ViewHolder(val binding: ItemViewAllOrderBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            binding.actionBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onActionClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            binding.callBtn.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onCallClicked?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
        }
    }

}