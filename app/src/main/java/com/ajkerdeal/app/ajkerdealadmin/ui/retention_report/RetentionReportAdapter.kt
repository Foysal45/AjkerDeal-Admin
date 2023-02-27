package com.ajkerdeal.app.ajkerdealadmin.ui.retention_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention_report.RetentionReportModel
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemRetentionReportBinding

class RetentionReportAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<RetentionReportModel> = mutableListOf()
    var onItemClicked: ((model: RetentionReportModel, flagApi : Int) -> Unit)? = null
    private var back: Int = 0
    private var advance: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val binding: ItemRetentionReportBinding = ItemRetentionReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewModel) {

            val model = dataList[position]
            val binding = holder.binding

            binding.srNameTv.text = model.fullName
            binding.regularMerchantTv.text = model.orderData.toString()
            binding.calledMerchantTv.text = model.merchantCalled.toString()
            binding.visitedMerchantTv.text = model.merchantVisited.toString()
            binding.todayOrderTv.text = model.orderDatanot.toString()
            binding.advanceOrderTv.text = model.orderDataFuture.toString()
            binding.key4.text = "Today Order \n(Not Placed Order in Back $back Days)"
            binding.key5.text = "Advance Order \n(Placed Order in Advance $advance Days)"

        }

    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewModel(val binding: ItemRetentionReportBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.calledMerchantTv.setOnClickListener{
                onItemClicked?.invoke(dataList[absoluteAdapterPosition], 1)
            }
            binding.visitedMerchantTv.setOnClickListener {
                onItemClicked?.invoke(dataList[absoluteAdapterPosition], 2)
            }
            binding.regularMerchantTv.setOnClickListener {
                onItemClicked?.invoke(dataList[absoluteAdapterPosition], 3)
            }
            binding.todayOrderTv.setOnClickListener {
                onItemClicked?.invoke(dataList[absoluteAdapterPosition], 4)
            }
            binding.advanceOrderTv.setOnClickListener {
                onItemClicked?.invoke(dataList[absoluteAdapterPosition], 5)
            }
        }

    }

    fun initLoad(list: List<RetentionReportModel>, back: Int, advance: Int) {
        dataList.clear()
        dataList.addAll(list)
        this.back = back
        this.advance = advance
        notifyDataSetChanged()
    }

}