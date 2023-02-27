package com.ajkerdeal.app.ajkerdealadmin.ui.track_order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.track_order.TrackOrderResponseModelItem
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class OrderTrackingAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<TrackOrderResponseModelItem> = mutableListOf()
    var onItemClick: ((model: TrackOrderResponseModelItem, position: Int) -> Unit)? = null
    var callEdesh: ((model: String) -> Unit)? = null
    var callDC: ((model: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view_order_track, parent, false))
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {

            val model = dataList[position]

            val formattedDate = DigitConverter.toBanglaDate(model.postedOn.trim(),"dd-MM-yyyy' 'HH:mm:ss", false)
            holder.orderTrackDate.text = formattedDate.trim()
            val formatTime = DigitConverter.formatDate(model.postedOn.trim(), "dd-MM-yyyy' 'HH:mm:ss", "hh:mm a")
            holder.orderTrackTime.text = DigitConverter.toBanglaDigit(formatTime.trim())

            holder.orderTrackStatus.text = model.statusNameBng + " (${model.status})"

            holder.commentedBy.text = "Commented by: ${model.namePostedBy}"
            if(model.status == 15 || model.status == 61){
                if(!model.courierDeliveryManMobile.isNullOrEmpty()){
                    holder.deliveryManMobileNumber.text = model.courierDeliveryManMobile
                    holder.deliveryManMobileNumber.visibility = View.VISIBLE
                }
                if(!model.districtsViewModel.edeshMobileNo.isNullOrEmpty()){
                    holder.edeshMobileNumber.text = model.districtsViewModel.edeshMobileNo
                    holder.edeshMobileNumber.visibility = View.VISIBLE
                }
            }

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderTrackDate: TextView = itemView.findViewById(R.id.date)
        val orderTrackStatus: TextView = itemView.findViewById(R.id.statusName)
        val orderTrackTime: TextView = itemView.findViewById(R.id.time)
        val commentedBy: TextView = itemView.findViewById(R.id.commentedBy)
        val deliveryManMobileNumber: TextView = itemView.findViewById(R.id.mobileNumber)
        val edeshMobileNumber: TextView = itemView.findViewById(R.id.dcMobileNumber)

        init {
            itemView.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            edeshMobileNumber.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    callEdesh?.invoke(dataList[absoluteAdapterPosition].districtsViewModel.edeshMobileNo!!)
                }
            }
            deliveryManMobileNumber.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    callDC?.invoke(dataList[absoluteAdapterPosition].courierDeliveryManMobile)
                }
            }
        }
    }

    fun initLoad(list: List<TrackOrderResponseModelItem>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        dataList.clear()
        notifyDataSetChanged()
    }

}