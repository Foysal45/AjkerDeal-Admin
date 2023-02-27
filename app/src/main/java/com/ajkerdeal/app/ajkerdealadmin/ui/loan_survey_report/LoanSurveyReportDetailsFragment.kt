package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.loansurvey_report.GetloanSurveyResponseBody
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentCompanyNameLoanSurveyBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLoanSurveyReportDetailsBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.DigitConverter

class LoanSurveyReportDetailsFragment : Fragment() {
    private var binding: FragmentLoanSurveyReportDetailsBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Details (${(arguments?.getParcelable("modelOfData") ?: GetloanSurveyResponseBody()).merchantName})"
        // Inflate the layout for this fragment
        return FragmentLoanSurveyReportDetailsBinding.inflate(layoutInflater).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        var model = arguments?.getParcelable("modelOfData") ?: GetloanSurveyResponseBody()
        binding?.apply {
            companyNameText.text = model.merchantName
            monthlyAverageTransactionText.text = model.transactionAmount.toString()
            TradelisenceNoAndExpireDateText.text =
                DigitConverter.formatDate(model.tradeLicenseExpireDate, "yyyy-MM-dd\'T\'HH:mm:ss", "dd MMM, YYYY")
            TinNoText.text = model.tinNumber
            EducationText.text = model.eduLevel
            GenderText.text = model.gender
            DOBText.text = DigitConverter.formatDate(model.dateOfBirth, "yyyy-MM-dd\'T\'HH:mm:ss", "dd MMM, YYYY")
            AgeText.text = model.age
            NidText.text = model.nidNo
            MarritualStatusText.text = model.married
            FamilyMemberNoText.text = model.famMem
            loanApplyDateText.text =  DigitConverter.formatDate(model.applicationDate, "yyyy-MM-dd\'T\'HH:mm:ss", "dd MMM, YYYY")
            interestedLoanAmountText.text = model.interestedAmount.toString()
            interestedLoanMonthCountText.text = model.reqTenorMonth.toString()
            kistiText.text = model.reqTenorMonth.toString()
            averageOrderText.text = model.monthlyOrder
            facebookIncomeText.text = model.transactionAmount.toString() //todo recheck this value
            physicalShopIncomeText.text = model.monthlyTotalAverageSale.toString()
            monthlyCodText.text = model.monthlyTotalCodAmount.toString()
            annualTotalIncomeText.text = model.annualTotalIncome.toString()
            othersIncomeText.text = model.othersIncome.toString()
            monthlyIncomeText.text = model.othersIncome.toString()
            averageBasketText.text = model.basketValue
            bankAccountBankNameAccountNumberText.text = model.companyBankAccName //todo add other fields
            creditCardText.text = model.hasCreditCard.toString()
            cardHolderBankText.text = model.cardHolder
            cardLimitText.text = model.cardLimit
            gurantorsNumberText.text = model.guarantorName
            otherLoanAmountText.text = model.loanAmount.toString()
            otherLoanBankNameText.text = model.bankName
            loanRepayTypeText.text = model.repayType
            knownToMerchantText.text = model.relationMarchent
            shopOwnershipText.text = model.shopOwnership
            commentText.text = model.recommend
            var allCourriers = ""
            model.couriersWithLoanSurveyViewModel.forEach {
                allCourriers += "${it.courierName}, "
            }
            servicewithoutdeliverytigerText.text = allCourriers

        }
    }
}