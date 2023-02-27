package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.bottomsheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.CourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.MonthlyTransactionLoanSurveyResponse
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewLoanSurveyReportBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.ItemViewMonthlyAverageTransactionLoanSurveyBinding

class MonthlyAverageTransactionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataList: MutableList<MonthlyTransactionLoanSurveyResponse> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(ItemViewMonthlyAverageTransactionLoanSurveyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val model = dataList[position]
            val binding = holder.binding
            binding.collectionAmount.text = model.totalCollectionAmount.toString()
            binding.MonthAmount.text = model.monthName
            binding.YearAmount.text = model.year.toString()
        }
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(val binding: ItemViewMonthlyAverageTransactionLoanSurveyBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
            }
        }
    }

    fun initLoad(list: List<MonthlyTransactionLoanSurveyResponse>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}