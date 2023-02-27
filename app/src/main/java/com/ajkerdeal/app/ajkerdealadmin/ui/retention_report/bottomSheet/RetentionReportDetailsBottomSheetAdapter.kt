package com.ajkerdeal.app.ajkerdealadmin.ui.retention_report.bottomSheet

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportDetailsModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemRetentionReportDetailsBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class RetentionReportDetailsBottomSheetAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<RetentionReportDetailsModel> = mutableListOf()
    var onItemClicked: ((model: RetentionReportDetailsModel) -> Unit)? = null
    var flagApi: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemRetentionReportDetailsBottomSheetBinding = ItemRetentionReportDetailsBottomSheetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.companyNameTv.text = model.companyName
            binding.userNameTv.text = model.userName

            if (flagApi==1){

                val liveDate = model.calledDate.split("T")?.first()
                val date = DigitConverter.formatDate(liveDate, "yyyy-MM-dd", "dd-MM-yyyy")

                binding.key2.visibility = View.VISIBLE
                binding.key3.visibility = View.VISIBLE
                binding.key4.visibility = View.VISIBLE
                binding.key5.visibility = View.VISIBLE
                binding.mobileTv.visibility = View.VISIBLE
                binding.callDurationTv.visibility = View.VISIBLE
                binding.calledSummaryTv.visibility = View.VISIBLE
                binding.calledDateTv.visibility = View.VISIBLE

                binding.IdTv.text = "ID: ${model.courierUserId}"
                binding.mobileTv.text = model.mobile
                binding.callDurationTv.text = model.callDuration.toString()
                binding.calledSummaryTv.text = model.calledSummary
                binding.calledDateTv.text = date

            } else if (flagApi == 2){

                val liveDate = model.visitedDate.split("T")?.first()
                val date = DigitConverter.formatDate(liveDate, "yyyy-MM-dd", "dd-MM-yyyy")

                binding.key6.visibility = View.VISIBLE
                binding.key7.visibility = View.VISIBLE
                binding.visitedSummaryTv.visibility = View.VISIBLE
                binding.visitedDateTv.visibility = View.VISIBLE

                binding.IdTv.text = "ID: ${model.courierUserId}"
                binding.visitedSummaryTv.text = model.visitedSummary
                binding.visitedDateTv.text = date

            } else if (flagApi == 3 || flagApi == 4 || flagApi == 5){

                binding.key2.visibility = View.VISIBLE
                binding.mobileTv.visibility = View.VISIBLE

                binding.IdTv.text = "ID: ${model.merchantId}"
                binding.mobileTv.text = model.mobile

            }

        }
    }

    inner class ViewModel(val binding: ItemRetentionReportDetailsBottomSheetBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClicked?.invoke(dataList[absoluteAdapterPosition])
            }
        }
    }

    fun initLoad(list: List<RetentionReportDetailsModel>, flagApi: Int) {
        dataList.clear()
        dataList.addAll(list)
        this.flagApi = flagApi
        notifyDataSetChanged()
    }

}