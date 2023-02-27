package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLoanSurveyReportBinding

class LoanSurveyReportAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<GetloanSurveyResponseBody> = mutableListOf()
    var onCompanyNameClicked: ((model: GetloanSurveyResponseBody) -> Unit)? = null
    var onAverageTransactionClicked: ((model: GetloanSurveyResponseBody) -> Unit)? = null
    var onRootClicked: ((model: GetloanSurveyResponseBody) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ItemViewLoanSurveyReportBinding = ItemViewLoanSurveyReportBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding
            binding.CompanyNameForInput.text = model.merchantName
            binding.averageTransactionText.text = model.collectionAmountAvg.toInt().toString()
            binding.interestedLoanAmountText.text = model.interestedAmount.toString()
            binding.EarningThroughFacebookText.text = model.monthlyTotalCodAmount.toString()
        }
    }

    inner class ViewHolder(val binding: ItemViewLoanSurveyReportBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
              binding.root.setOnClickListener {
                   onRootClicked?.invoke(dataList[absoluteAdapterPosition])
               }
            binding.CompanyNameForInput.setOnClickListener {
                onCompanyNameClicked?.invoke(dataList[absoluteAdapterPosition])
            }
            binding.averageTransactionText.setOnClickListener {
                onAverageTransactionClicked?.invoke(dataList[absoluteAdapterPosition])
            }
        }
    }

    fun initLoad(list: List<GetloanSurveyResponseBody>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}