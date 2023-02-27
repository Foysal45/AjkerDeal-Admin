package com.ajkerdeal.app.ajkerdealadmin.ui.operation.complain_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.complain.complain_report.AutoAssignComplainCountResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainListBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewComplainReportBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.DtComplainListAdapter
import java.text.SimpleDateFormat
import java.util.*

class ComplainReportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<AutoAssignComplainCountResponse> = mutableListOf()
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    var onItemClick: ((model: AutoAssignComplainCountResponse, position: Int) -> Unit)? = null
    var onTotalClick: ((model: AutoAssignComplainCountResponse) -> Unit)? = null
    var onPendingClick: ((model: AutoAssignComplainCountResponse) -> Unit)? = null
    var onSolvedClick: ((model: AutoAssignComplainCountResponse) -> Unit)? = null
    var onTouchedClick: ((model: AutoAssignComplainCountResponse) -> Unit)? = null
    var onUntouchedClick: ((model: AutoAssignComplainCountResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewComplainReportBinding = ItemViewComplainReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ComplainReportAdapter.ViewModel) {
            val model = dataList[position]
            val binding = holder.binding

            binding.assignedTo.text = model.fullName
            binding.totalComplainTV.text = model.totalComplain.toString()
            binding.perndingComplainTV.text = model.pendingComplain.toString()
            binding.solvedComplainTV.text = model.solvedComplain.toString()
            binding.touchedComplainTV.text = model.touchedComplain.toString()
            binding.untouchedComplainTV.text = model.untouchedComplain.toString()


        }
    }

    internal inner class ViewModel(val binding: ItemViewComplainReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

            binding.root.setOnClickListener {
                if (absoluteAdapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(dataList[absoluteAdapterPosition], absoluteAdapterPosition)
                }
            }
            binding.totalComplainTV.setOnClickListener {
                onTotalClick?.invoke(dataList[absoluteAdapterPosition])
            }
            binding.perndingComplainTV.setOnClickListener {
                onPendingClick?.invoke(dataList[absoluteAdapterPosition])
            }
            binding.solvedComplainTV.setOnClickListener {
                onSolvedClick?.invoke(dataList[absoluteAdapterPosition])
            }
            binding.touchedComplainTV.setOnClickListener {
                onTouchedClick?.invoke(dataList[absoluteAdapterPosition])
            }
            binding.untouchedComplainTV.setOnClickListener {
                onUntouchedClick?.invoke(dataList[absoluteAdapterPosition])
            }

        }
    }

    fun initLoad(list: List<AutoAssignComplainCountResponse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun pagingLoad(list: List<AutoAssignComplainCountResponse>) {
        val currentIndex = dataList.size
        val newDataCount = list.size
        dataList.addAll(list)
        notifyItemRangeInserted(currentIndex, newDataCount)
    }

}