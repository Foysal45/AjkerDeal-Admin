package com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.CourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.SelectedCourierModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.loan_survey.UpdateLoanSurveyRequest
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.loan_survey_mock.TokenGenerateUserLoginRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLoanSurveyBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey.adapter.LocalUniversalAdapter
import com.ajkerdeal.app.ajkerdealadmin.utils.*
import com.ajkerdeal.app.ajkerdealadmin.utils.Validator.isValidMobileNumber
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.android.ext.android.inject
import timber.log.Timber
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class LoanSurveyFragment : Fragment() {

    private var binding: FragmentLoanSurveyBinding? = null
    private val viewModel: LoanSurveryViewModel by inject()

    private val dataAdapter = LoanSurveyAdapter()

    private var courierList: MutableList<CourierModel> = mutableListOf()
    private var selectedCourierList: MutableList<SelectedCourierModel> = mutableListOf()

    private var merchantName = ""
    private var merchantGender = ""

    private var loanRange = ""
    private var loanRepayMonthPeriod = ""
    private var yearlyTotalIncome = 0
    private var otherIncome = ""
    private var nidCardNo = ""
    private var monthlyTransaction = ""

    private var totalMonthlyCOD = ""
    private var totalMonthlyAverageSell = ""
    private var previousTakingLoanAmount = 0
    private var globalIDFOrLoan = 0
    private var bankName = ""
    private var guarantorName = ""
    private var guarantorNumber = ""

    private var hasCreditCard = false
    private var hasTin = false
    private var hasBankAccount = false
    private var hasPhysicalShop = false
    private var hasTradeLicence = false
    private var hasGuarantor = false
    private var hasPreviousLoan = false
    private var isExpireDateofTradeLisenseSelectd = false

    private var imagePath: String = ""
    private var imagePickFlag = 0
    private var imageTradeLicencePath: String = ""

    private var selectedEducation = ""
    private var selectedAverageBasket = ""
    private var selectedKnownMerchantDuration = ""
    private var selectedAverageOrder = ""
    private var selectedMonthlyExp = ""
    private var selectedCurrentLoanEMI = ""
    private var selectedOwnerShipOfMarket = ""

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val sdf1 = SimpleDateFormat("dd MMM, yyyy", Locale.US)

    private var selectedDateTradeLisence = ""
    private var selectedDateDOB = ""
    private var selectedDateFormattedTradeLisence = ""
    private var selectedDateFormattedDOB = ""
    private var applicationDate = ""
    private var fetchedToken = ""
    private var tradeLicenseImageUrl = ""

    var merchantId: Int = 0
    var hasPreviousData = false


    private val adapterAge = LocalUniversalAdapter()
    private val familyMemNumAdapter = LocalUniversalAdapter()
    private val locationAdapter = LocalUniversalAdapter()
    private val marriageStatusAdapter = LocalUniversalAdapter()
    private val houseOwnerAdapter = LocalUniversalAdapter()


    /* override fun onResume() {
         super.onResume()
         (activity as HomeActivity).setToolbarTitle(getString(R.string.loan_survey))
     }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "লোন সার্ভে আপডেট"
        return FragmentLoanSurveyBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* if (SessionManager.isSurveyUpdateComplete) {
             warning()
         }*/
        init()
        fetchBanner()
        initData()
        initClickListener()
    }

    private fun initViews() {
        ageRecycler()
        setUpEduactionSpinner()
        familyMemNumRecycler()
        homeLocationRecycler()
        marriageStatusRecycler()
        setUpAverageBasketSpinner()
        setUpSpinnerKnownToMerchnatSpinner()
        houseOwnerRecycler()
        setUpSpinnerAverageOrderSpinner()
        setUpSpinnerMonthlyExpSpinner()
        setUpSpinnerCurrentLoanEMISpinner()
        fetchCourierList()
        setUpEduactionSpinner()
    }

    private fun initData() {
        merchantId = arguments?.getInt("courierUserId") ?: 0
        val mobileOfUser = arguments?.getString("mobileNumber") ?: ""
        val passwordOfUser = arguments?.getString("userPassword") ?: ""
        hasPreviousData = arguments?.getBoolean("hasData") ?: false
        if (hasPreviousData) {
            viewModel.tokenGenerateUserLogin(
                TokenGenerateUserLoginRequest(
                    courierUserId = merchantId,
                    mobile = mobileOfUser,
                    password = passwordOfUser
                )
            ).observe(viewLifecycleOwner, {
                fetchedToken = it.token
            })
            viewModel.getLoanSurveyData(merchantId).observe(viewLifecycleOwner, { model ->
                if (!model.first().applicationDate.isNullOrEmpty()) {
                    applicationDate = model.first().applicationDate
                }
                binding?.merchantGenderRadioGroup?.check(if (model.first().gender == "male") R.id.merchantGenderMale else R.id.merchantGenderFemale)
                if (!model.first().merchantName.isNullOrEmpty()) {
                    merchantName = model.first().merchantName
                    binding?.merchantNameET?.setText(model.first().merchantName)
                }
                if (!model.first().nidNo.isNullOrEmpty()) {
                    binding?.nidCardNoET?.setText(model.first().nidNo)
                }
                if (!model.first().dateOfBirth.isNullOrEmpty()) {
                    binding?.DOBET?.setText(DigitConverter.formatDate(model.first().dateOfBirth, "yyyy-MM-dd", "dd MMM yyyy"))
                }
                globalIDFOrLoan = model.first().loanSurveyId
                binding?.loanAMountET?.setText(model.first().loanAmount.toInt().toString())
                binding?.loanRangeET?.setText(model.first().interestedAmount.toInt().toString())
                binding?.reqTenorMonthET?.setText(model.first().reqTenorMonth.toInt().toString())
                binding?.yearlyTotalIncomeET?.setText(model.first().annualTotalIncome.toInt().toString())
                binding?.otherIncomeET?.setText(model.first().othersIncome.toInt().toString())
                binding?.monthlyFBTransactionET?.setText(model.first().transactionAmount.toInt().toString())
                binding?.totalCODFromOtherServicesET?.setText(model.first().monthlyTotalCodAmount.toInt().toString())
                binding?.merchantPhysicalShopExistsRadioGroup?.check(if (model.first().isLocalShop) R.id.merchantPhysicalShopExistsYes else R.id.merchantPhysicalShopExistsNo)
                binding?.totalMonthlyAverageSellET?.setText(model.first().monthlyTotalAverageSale.toInt().toString())
                binding?.merchantTakeLoanRadioGroup?.check(
                    if (model.first().hasPreviousLoan) {
                        R.id.merchantTakeLoanAccountYes
                    } else {
                        R.id.merchantTakeLoanAccountNo
                    }
                )

                if (!model.first().bankName.isNullOrEmpty()) {
                    binding?.bankNameET?.setText(model.first().bankName)
                }
                if (!model.first().companyBankAccName.isNullOrEmpty()) {
                    binding?.companyBankNameTextInput?.setText(model.first().companyBankAccName)
                }
                if (!model.first().companyBankAccNo.isNullOrEmpty()) {
                    binding?.bankAccountNumberET?.setText(model.first().companyBankAccNo)
                }
                if (!model.first().cardHolder.isNullOrEmpty()) {
                    binding?.creditCardName?.setText(model.first().cardHolder)
                }
                if (!model.first().cardLimit.isNullOrEmpty()) {
                    binding?.creditCardLimit?.setText(model.first().cardLimit)
                }
                if (!model.first().tinNumber.isNullOrEmpty()) {
                    binding?.teamTINNumberET?.setText(model.first().tinNumber)
                }
                if (!model.first().tradeLicenseNo.isNullOrEmpty()) {
                    binding?.tradeLicenseNoTV?.setText(model.first().tradeLicenseNo)
                }
                if (!model.first().guarantorName.isNullOrEmpty()) {
                    binding?.merchantGuarantorNameET?.setText(model.first().guarantorName)
                }
                if (!model.first().guarantorMobile.isNullOrEmpty()) {
                    binding?.merchantGuarantorNumberET?.setText(model.first().guarantorMobile)
                }
                if (!model.first().tradeLicenseExpireDate.isNullOrEmpty()) {
                    selectedDateTradeLisence = model.first().tradeLicenseExpireDate
                }
                if (!model.first().tradeLicenseExpireDate.isNullOrEmpty()) {
                    binding?.tradeLicenseExpireDateTV?.setText(DigitConverter.formatDate(model.first().tradeLicenseExpireDate, "yyyy-MM-dd", "dd MMM yyyy"))
                }
                if (!model.first().eduLevel.isNullOrEmpty()) {
                    setUpEduactionSpinner(model.first().eduLevel, true)
                } else {
                    setUpEduactionSpinner(model.first().eduLevel, false)
                }
                if (!model.first().basketValue.isNullOrEmpty()) {
                    setUpAverageBasketSpinner(model.first().basketValue, true)
                } else {
                    setUpAverageBasketSpinner(model.first().basketValue, false)
                }
                if (!model.first().monthlyExp.isNullOrEmpty()) {
                    setUpSpinnerMonthlyExpSpinner(model.first().monthlyExp, true)
                } else {
                    setUpSpinnerMonthlyExpSpinner(model.first().monthlyExp, false)
                }
                if (!model.first().relationMarchent.isNullOrEmpty()) {
                    setUpSpinnerKnownToMerchnatSpinner(model.first().relationMarchent, true)
                } else {
                    setUpSpinnerKnownToMerchnatSpinner(model.first().relationMarchent, false)
                }
                if (!model.first().loanEmi.isNullOrEmpty()) {
                    setUpSpinnerCurrentLoanEMISpinner(model.first().loanEmi, true)
                } else {
                    setUpSpinnerCurrentLoanEMISpinner(model.first().loanEmi, false)
                }
                if (!model.first().monthlyOrder.isNullOrEmpty()) {
                    setUpSpinnerAverageOrderSpinner(model.first().monthlyOrder, true)
                } else {
                    setUpSpinnerAverageOrderSpinner(model.first().monthlyOrder, false)
                }
                if (!model.first().famMem.isNullOrEmpty()) {
                    familyMemNumRecycler(model.first().famMem, true)
                } else {
                    familyMemNumRecycler(model.first().famMem, false)
                }
                if (!model.first().homeOwnership.isNullOrEmpty()) {
                    houseOwnerRecycler(model.first().homeOwnership, true)
                } else {
                    houseOwnerRecycler(model.first().homeOwnership, false)
                }
                if (!model.first().residenceLocation.isNullOrEmpty()) {
                    homeLocationRecycler(model.first().residenceLocation, true)
                } else {
                    homeLocationRecycler(model.first().residenceLocation, false)
                }
                if (!model.first().married.isNullOrEmpty()) {
                    marriageStatusRecycler(model.first().married, true)
                } else {
                    marriageStatusRecycler(model.first().married, false)
                }
                if (!model.first().age.isNullOrEmpty()) {
                    ageRecycler(model.first().age, true)
                } else {
                    ageRecycler(model.first().age, false)
                }
                setDateRangePickerTitleTradeLisencee(0, true, model.first().tradeLicenseExpireDate)
                setDateRangePickerTitleDOB(0, true, model.first().dateOfBirth)
                binding?.shopOwnershipInBusinessRadioGroup?.check(
                    when (model.first().shopOwnership) {
                        "নিজের" -> {
                            R.id.InBusinessRadioButtonOwner
                        }
                        "ভাড়া" -> {
                            R.id.InBusinessRadioButtonRental
                        }
                        "পরিবারের নিজস্ব" -> {
                            R.id.InBusinessRadioButtonFamily
                        }
                        else -> {
                            R.id.InBusinessRadioButtonOwner
                        }
                    }
                )
                binding?.merchantHasGuarantorRadioGroup?.check(
                    if (!model.first().guarantorMobile.isNullOrEmpty() && !model.first().guarantorName.isNullOrEmpty()) {
                        R.id.merchantHasGuarantorYes
                    } else {
                        R.id.merchantHasGuarantorNo
                    }
                )
                binding?.haveAnyCreditCardRadioGroup?.check(
                    if (model.first().hasCreditCard) {
                        R.id.yes_haveAnyCreditCard_radio_button
                    } else {
                        R.id.no_haveAnyCreditCard_radio_button
                    }
                )
                binding?.haveAnyTINRadioGroup?.check(
                    if (model.first().hasTin) {
                        R.id.yes_haveAnyTin_radio_button
                    } else {
                        R.id.no_haveAnyTin_radio_button
                    }
                )
                binding?.merchantHasTradeLicenceRadioGroup?.check(
                    if (model.first().hasTradeLicense) {
                        R.id.merchantHasTradeLicenceYes
                    } else {
                        R.id.merchantHasTradeLicenceNo
                    }
                )
                binding?.loanRepayRadioGroupType?.check(
                    if (model.first().repayType == "weekly") {
                        R.id.loanRepayWeekly
                    } else {
                        R.id.loanRepayMonthly
                    }
                )
                binding?.merchantHasBankAccountRadioGroup?.check(
                    if (model.first().isBankAccount) {
                        R.id.merchantHasBankAccountYes
                    } else {
                        R.id.merchantHasBankAccountNo
                    }
                )
                if (!model.first().tradeLicenseImageUrl.isNullOrEmpty()) {
                    binding?.imageTradeLicenceAddIV?.isVisible = false
                    binding?.imageTradeLicencePickedIV?.isVisible = true
                    binding?.imageTradeLicencePickedIV?.let { image ->
                        Glide.with(image)
                            .load(model.first().tradeLicenseImageUrl)
                            .apply(RequestOptions().placeholder(R.drawable.ic_banner_place))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(image)
                    }
                    tradeLicenseImageUrl = model.first().tradeLicenseImageUrl
                }
                courierList.clear()
                viewModel.fetchCourierList().observe(viewLifecycleOwner, Observer { list ->
                    if (!list.isNullOrEmpty()) {
                        for (index in list.indices) {
                            model.first().courierWithLoanSurvey.forEach { couriersOfCurrentUser ->
                                if (couriersOfCurrentUser.courierId == list[index].courierId
                                ) {
                                    dataAdapter.multipleSelection(list[index], index)
                                }
                            }
                        }
                        courierList.addAll(list)
                        dataAdapter.initLoad(courierList)
                    }
                })
            })
        } else {
            initViews()
        }
    }

    private fun init() {
        binding?.recyclerViewOtherServices?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                adapter = dataAdapter
                itemAnimator = null
            }
        }
    }

    private fun initClickListener() {

        dataAdapter.onItemClicked = { model, position ->
            dataAdapter.multipleSelection(model, position)
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        binding?.applyLoanSurveyUpdateBtn?.setOnClickListener {
            if (verify()) {

                var requestBody = UpdateLoanSurveyRequest(
                    age = adapterAge.selectedItem,
                    annualTotalIncome = yearlyTotalIncome ?: 0,
                    bankName = binding?.bankNameET?.text.toString(),
                    basketValue = selectedAverageBasket,
                    cardHolder = binding?.creditCardName?.text.toString(),
                    cardLimit = binding?.creditCardLimit?.text.toString(),
                    companyBankAccName = binding?.companyBankNameTextInput?.text.toString().trim(),
                    companyBankAccNo = binding?.bankAccountNumberET?.text.toString().trim(),
                    loanSurveyId = globalIDFOrLoan,
                    dateOfBirth = selectedDateDOB,
                    eduLevel = selectedEducation,
                    famMem = familyMemNumAdapter.selectedItem,
                    gender = merchantGender,
                    guarantorMobile = guarantorNumber,
                    guarantorName = guarantorName,
                    hasCreditCard = binding?.haveAnyCreditCardRadioGroup?.checkedRadioButtonId == R.id.yes_haveAnyCreditCard_radio_button,
                    hasTin = binding?.haveAnyTINRadioGroup?.checkedRadioButtonId == R.id.yes_haveAnyTin_radio_button,
                    hasTradeLicense = binding?.merchantHasTradeLicenceRadioGroup?.checkedRadioButtonId == R.id.merchantHasTradeLicenceYes,
                    homeOwnership = houseOwnerAdapter.selectedItem,
                    interestedAmount = loanRange.toDouble().toInt(),
                    isBankAccount = hasBankAccount,
                    isLocalShop = hasPhysicalShop,
                    loanAmount = if (hasPreviousLoan) previousTakingLoanAmount else 0,
                    loanEmi = selectedCurrentLoanEMI,
                    married = marriageStatusAdapter.selectedItem,
                    merchantName = merchantName,
                    monthlyExp = selectedMonthlyExp,
                    monthlyOrder = selectedAverageOrder,
                    monthlyTotalAverageSale = if (hasPhysicalShop) totalMonthlyAverageSell.toDouble().toInt() else 0,
                    monthlyTotalCodAmount = totalMonthlyCOD.toDouble().toInt(),
                    nidNo = nidCardNo,
                    othersIncome = binding?.otherIncomeET?.text.toString().trim().toDouble().toInt() ?: 0,
                    recommend = "",
                    relationMarchent = selectedKnownMerchantDuration,
                    repayType = if (binding?.loanRepayRadioGroupType?.checkedRadioButtonId == R.id.loanRepayWeekly) "weekly" else "monthly",
                    reqTenorMonth = loanRepayMonthPeriod.toInt(),
                    residenceLocation = locationAdapter.selectedItem,
                    shopOwnership = selectedOwnerShipOfMarket,
                    tinNumber = binding?.teamTINNumberET?.text.toString().trim(),
                    tradeLicenseExpireDate = selectedDateTradeLisence,
                    tradeLicenseImageUrl = tradeLicenseImageUrl,
                    tradeLicenseNo = binding?.tradeLicenseNoTV?.text.toString().trim(),
                    transactionAmount = monthlyTransaction.toDouble().toInt(),
                    hasPreviousLoan = hasPreviousLoan,
                    applicationDate = if (applicationDate == "") sdf.format(Calendar.getInstance().time) else applicationDate,
                    courierUserId = merchantId
                )
                if (imagePickFlag == 1) {
                    requestBody.apply {
                        tradeLicenseImageUrl =
                            "https://static.ajkerdeal.com/delivery_tiger/trade_license/trade_${merchantId}.jpg"
                    }
                    if (imageTradeLicencePath != "") {
                        uploadImage(
                            "trade_${merchantId}.jpg",
                            "delivery_tiger/trade_license",
                            imageTradeLicencePath, requestBody
                        )
                    } else {
                        submitUpdatedLoanSurveyData(requestBody)
                    }
                } else {
                    submitUpdatedLoanSurveyData(requestBody)
                }
            }

        }
        binding?.tradeLicenseExpireDateTV?.setOnClickListener {
            datePickerTradeLisence()
        }
        //region merchantGender
        binding?.merchantGenderRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantGenderMale -> {
                    merchantGender = "male"
                }
                R.id.merchantGenderFemale -> {
                    merchantGender = "female"
                }
            }
            binding?.merchantNameETLayout?.isVisible = true
        }


        //region merchantHasBankAccount
        binding?.merchantHasBankAccountRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantHasBankAccountYes -> {
                    hasBankAccount = true
                    binding?.hasBankAccountLayout?.isVisible = true
                    binding?.companyBankNameLayout?.isVisible = true
                    binding?.bankAccountNumberLayout?.isVisible = true
                    binding?.companyBankNameTextInput?.isVisible = true
                    binding?.bankAccountNumberET?.isVisible = true
                }
                R.id.merchantHasBankAccountNo -> {
                    binding?.bankAccountNumberET?.setText("")
                    binding?.companyBankNameTextInput?.setText("")
                    binding?.companyBankNameTextInput?.isVisible = false
                    binding?.bankAccountNumberET?.isVisible = false
                    binding?.hasBankAccountLayout?.isVisible = false
                    binding?.companyBankNameLayout?.isVisible = false
                    binding?.bankAccountNumberLayout?.isVisible = false
                }
            }
        }

        binding?.DOBET?.setOnClickListener {
            datePickerDOB()
        }


        //region merchantPhysicalShopExists
        binding?.merchantPhysicalShopExistsRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantPhysicalShopExistsYes -> {
                    hasPhysicalShop = true
                    binding?.totalMonthlyAverageSellETLayout?.isVisible = true
                }
                R.id.merchantPhysicalShopExistsNo -> {
                    hasPhysicalShop = false
                    binding?.totalMonthlyAverageSellETLayout?.isVisible = false
                }
            }
        }

        //region merchantTradeLicence
        binding?.merchantHasTradeLicenceRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantHasTradeLicenceYes -> {
                    hasTradeLicence = true
                    imagePickFlag = 1
                    binding?.merchantTradeLicenceLayout?.isVisible = true
                    binding?.tradeLicenseNoandExpireDateLayout?.visibility = View.VISIBLE
                    binding?.merchantTradeLicenceLayout?.visibility = View.VISIBLE
                    binding?.tradeLicenseNoLayout?.visibility = View.VISIBLE
                    binding?.tradeLicenseNoTV?.visibility = View.VISIBLE
                    binding?.tradeLicenseExpireDateLayout?.visibility = View.VISIBLE
                    binding?.tradeLicenseExpireDateTV?.visibility = View.VISIBLE
                }
                R.id.merchantHasTradeLicenceNo -> {
                    tradeLicenseImageUrl = ""
                    binding?.tradeLicenseNoTV?.setText("")
                    binding?.tradeLicenseExpireDateTV?.setText("")
                    selectedDateTradeLisence = ""
                    hasTradeLicence = false
                    imagePickFlag = 0
                    binding?.merchantTradeLicenceLayout?.isVisible = false
                    binding?.imageTradeLicenceAddIV?.isVisible = true
                    binding?.imageTradeLicencePickedIV?.isVisible = false
                    binding?.tradeLicenseNoandExpireDateLayout?.visibility = View.GONE
                    binding?.merchantTradeLicenceLayout?.visibility = View.GONE
                    binding?.tradeLicenseNoLayout?.visibility = View.GONE
                    binding?.tradeLicenseNoTV?.visibility = View.GONE
                    binding?.tradeLicenseExpireDateLayout?.visibility = View.GONE
                    binding?.tradeLicenseExpireDateTV?.visibility = View.GONE

                }
            }
        }

        binding?.merchantTradeLicenceLayout?.setOnClickListener {
            pickImage()
        }

        //region merchantHasGuarantor
        binding?.merchantHasGuarantorRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantHasGuarantorYes -> {
                    hasGuarantor = true
                    binding?.merchantGuarantorLayout?.isVisible = true
                }
                R.id.merchantHasGuarantorNo -> {
                    binding?.merchantGuarantorNameET?.setText("")
                    binding?.merchantGuarantorNumberET?.setText("")
                    hasGuarantor = false
                    binding?.merchantGuarantorLayout?.isVisible = false
                }
            }
        }

        // region merchantPreviousLoan
        binding?.merchantTakeLoanRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.merchantTakeLoanAccountYes -> {
                    hasPreviousLoan = true
                    binding?.merchantLoanAmountETLayout?.isVisible = true
                    binding?.bankNameETETLayout?.isVisible = true
                    binding?.loanRepayMonthlyLayout?.isVisible = true
                    binding?.spinnerCurrentLoanEMILayout?.isVisible = true
                }
                R.id.merchantTakeLoanAccountNo -> {
                    binding?.loanAMountET?.setText("")
                    binding?.bankNameET?.setText("")
                    hasPreviousLoan = false
                    binding?.merchantLoanAmountETLayout?.isVisible = false
                    binding?.bankNameETETLayout?.isVisible = false
                    binding?.loanRepayMonthlyLayout?.isVisible = false
                    binding?.spinnerCurrentLoanEMILayout?.isVisible = false
                }
            }
        }

        binding?.shopOwnershipInBusinessRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.InBusinessRadioButtonOwner -> {
                    selectedOwnerShipOfMarket = "নিজের"

                }
                R.id.InBusinessRadioButtonRental -> {
                    selectedOwnerShipOfMarket = "ভাড়া"

                }
                R.id.InBusinessRadioButtonFamily -> {
                    selectedOwnerShipOfMarket = "পরিবারের নিজস্ব"

                }
            }
        }

        binding?.apply {
            haveAnyCreditCardRadioGroup?.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.yes_haveAnyCreditCard_radio_button -> {
                        hasCreditCard = true
                        bankLayoutVisibility?.isVisible = true
                        cardLimitLayout?.isVisible = true
                        bankNameLayout?.isVisible = true
                        creditCardName?.isVisible = true
                        creditCardLimit?.isVisible = true
                    }
                    R.id.no_haveAnyCreditCard_radio_button -> {
                        binding?.creditCardLimit?.setText("")
                        binding?.creditCardName?.setText("")
                        hasCreditCard = false
                        bankLayoutVisibility?.isVisible = false
                        cardLimitLayout?.isVisible = false
                        bankNameLayout?.isVisible = false
                        creditCardName?.isVisible = false
                        creditCardLimit?.isVisible = false
                    }
                }
            }
        }
        binding?.apply {
            haveAnyTINRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.yes_haveAnyTin_radio_button -> {
                        hasTin = true
                        TIINNumberLayout.isVisible = true
                        teamTINNumberET.isVisible = true
                    }
                    R.id.no_haveAnyTin_radio_button -> {
                        binding?.teamTINNumberET?.setText("")
                        hasTin = false
                        TIINNumberLayout.isVisible = false
                        teamTINNumberET.isVisible = false
                    }
                }
            }
        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            //.compress(200)
            .crop(1.5f, 2f)
            .createIntent { intent ->
                startImagePickerResult.launch(intent)
            }
    }

    private val startImagePickerResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data!!

                when (imagePickFlag) {
                    1 -> {
                        binding?.imageTradeLicenceAddIV?.isVisible = false
                        binding?.imageTradeLicencePickedIV?.isVisible = true

                        imagePath = uri.path ?: ""
                        binding?.imageTradeLicencePickedIV?.let { view ->
                            Glide.with(requireContext())
                                .load(imagePath)
                                .apply(RequestOptions().placeholder(R.drawable.ic_banner_place))
                                .into(view)
                        }
                        imageTradeLicencePath = imagePath
                        tradeLicenseImageUrl = "https://static.ajkerdeal.com/delivery_tiger/trade_license/trade_${merchantId}.jpg"
                    }
                    2 -> {
                        imagePickFlag = 0
                    }
                    3 -> {
                        imagePath = uri.path ?: ""
                        imagePickFlag = 0
                    }
                    else -> {
                        imagePickFlag = 0
                    }
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                context?.toast(ImagePicker.getError(data))
                imagePickFlag = 0
            }
        }

    private fun uploadImage(fileName: String, imagePath: String, fileUrl: String, requestBody: UpdateLoanSurveyRequest) {

        val progressDialog = progressDialog()
        progressDialog.show()

        viewModel.imageUploadForFile(requireContext(), fileName, imagePath, fileUrl)
            .observe(viewLifecycleOwner, Observer { model ->
                progressDialog.dismiss()
                if (model) {
                    submitUpdatedLoanSurveyData(requestBody)
                    context?.toast("Uploaded successfully")
                } else {
                    context?.toast("Uploaded Failed")
                }
            })
    }

    private fun submitUpdatedLoanSurveyData(requestBody: UpdateLoanSurveyRequest) {
        val progressDialog = progressDialog()
        progressDialog.show()
        progressDialog.dismiss()
        progressDialog.setCanceledOnTouchOutside(false)
        if (hasPreviousData) {
            viewModel.updateLoanSurvey(requestBody, fetchedToken).observe(viewLifecycleOwner, Observer { isSuccess ->
                SessionManager.isSurveyUpdateComplete = isSuccess
                if (isSuccess) {
                    for (item in dataAdapter.getSelectedItemModelList()) {
                        item.loanSurveyId = this.globalIDFOrLoan
                        selectedCourierList.add(item)
                    }
                    viewModel.updateCourierWithLoanSurvey(selectedCourierList, this.globalIDFOrLoan, fetchedToken)
                    progressDialog.dismiss()
                    findNavController().popBackStack()
                    showAlert()
                }
            })
        } else {
            viewModel.submitLoanSurvey(requestBody).observe(viewLifecycleOwner, Observer { model ->
                val tempLoanSurveyId =
                    if (globalIDFOrLoan == 0) model.loanSurveyId else globalIDFOrLoan
                selectedCourierList.clear()
                for (item in dataAdapter.getSelectedItemModelList()) {
                    item.loanSurveyId = tempLoanSurveyId
                    selectedCourierList.add(item)
                }
                viewModel.submitCourierList(selectedCourierList)
                progressDialog.dismiss()
                findNavController().popBackStack()
                showAlert()
            })
        }
    }

    private fun showAlert() {
        val titleText = "নির্দেশনা"
        val descriptionText = "সার্ভেটি আপডেট করার জন্য ধন্যবাদ।"
        alert(titleText, descriptionText, false, "ঠিক আছে", "না").show()
    }

    private fun warning() {
        val titleText = "নির্দেশনা"
        val descriptionText = "আপনি ইতিপূর্বে একবার সার্ভেটি আপডেট করেছেন, আপনি কি আবার আপডেট করতে ইচ্ছুক?"
        alert(titleText, descriptionText, false, "হ্যাঁ") {
            if (it == AlertDialog.BUTTON_POSITIVE) {
//                findNavController().popBackStack()
            }
        }.apply {
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun fetchBanner() {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_banner_place)
            .signature(ObjectKey(Calendar.getInstance().get(Calendar.DAY_OF_YEAR).toString()))
        binding?.bannerImage?.let { image ->
            Glide.with(image)
                .load("https://static.ajkerdeal.com/images/merchant/loan_banner.jpg")
                .apply(options)
                .into(image)
        }
    }

    private fun ageRecycler(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val dataListAge: MutableList<String> = mutableListOf()
        dataListAge.add("১৮-২৫")
        dataListAge.add("২৫-৩০")
        dataListAge.add("৩০-৩৫")
        dataListAge.add("৩৫-৬৫")

        val indexOfSelectedItem = dataListAge.indexOf(preselectedItem)
        adapterAge.initLoad(dataListAge, indexOfSelectedItem, hasPreviousSelection)
        binding?.ageRecyclerView?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
                adapter = adapterAge
                itemAnimator = null
            }
        }

    }

    private fun familyMemNumRecycler(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val education: List<String> = listOf(
            "২-৫",
            "৫-৭",
            "৭-১০",
            "১০-১৫"
        )
        val indexOfselectedItem = education.indexOf(preselectedItem)
        familyMemNumAdapter.initLoad(education, indexOfselectedItem, hasPreviousSelection)
        binding?.FamilyMemNumRecyclerView?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
                adapter = familyMemNumAdapter
                itemAnimator = null
            }
        }
    }

    private fun homeLocationRecycler(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val location: List<String> = listOf(
            "মহানগর",
            "সিটি কর্পোরেশন",
            "শহরে",
            "গ্রামে"
        )
        val indexOfselectedItem = location.indexOf(preselectedItem)

        locationAdapter.initLoad(location, indexOfselectedItem, hasPreviousSelection)
        binding?.residenceLocationRecyclerView?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false)
                adapter = locationAdapter
                itemAnimator = null
            }
        }
    }

    private fun marriageStatusRecycler(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val location: List<String> = listOf(
            "বিবাহিত",
            "অবিবাহিত",
            "তালাকপ্রাপ্ত",
            "বিধবা/ বিপত্নীক"
        )
        val indexOfselectedItem = location.indexOf(preselectedItem)
        marriageStatusAdapter.initLoad(location, indexOfselectedItem, hasPreviousSelection)
        binding?.marriageRecyclerView?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                adapter = marriageStatusAdapter
                itemAnimator = null
            }
        }
    }

    private fun houseOwnerRecycler(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {
        val houseOwner: List<String> = listOf(
            "নিজের",
            "পরিবারের নিজস্ব",
            "ভাড়া"
        )
        val indexOfselectedItem = houseOwner.indexOf(preselectedItem)

        houseOwnerAdapter.initLoad(houseOwner, indexOfselectedItem, hasPreviousSelection)
        binding?.houseOwnerRecyclerView?.let { recyclerView ->
            recyclerView.apply {
                layoutManager =
                    GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)
                adapter = houseOwnerAdapter
                itemAnimator = null
            }
        }
    }

    private fun setUpEduactionSpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val dataListAge: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "প্রাতিষ্ঠানিক শিক্ষা নেই",
            "পি এস সি",
            "জে এস সি",
            "এস এস সি",
            "এইচ এস সি",
            "স্নাতক",
            "স্নাতকোত্তর"
        )
        val indexOfselectedItem = dataListAge.indexOf(preselectedItem)

        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, dataListAge)

        binding?.spinnereducationType?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.spinnereducationType?.setSelection(indexOfselectedItem)
        }
        binding?.spinnereducationType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedEducation = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun setUpAverageBasketSpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val AverageBasket: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "১০০০০০-২০০০০০",
            "২০০০০০-৪০০০০০",
            "৪০০০০০-৬০০০০০",
            "৬০০০০০-৮০০০০০",
            "৮০০০০০-১০০০০০০",
            "১০০০০০০-১২০০০০০"
        )
        val indexOfselectedItem = AverageBasket.indexOf(preselectedItem)
        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, AverageBasket)
        binding?.spinneraverageBasketType?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.spinneraverageBasketType?.setSelection(indexOfselectedItem)
        }
        binding?.spinneraverageBasketType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedAverageBasket = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun setUpSpinnerKnownToMerchnatSpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val knownToMerchant: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "০-১",
            "১-২",
            "২-৪",
            "৪-৬",
            "৬-৮",
            "৮-১০",
            "১০-১৫",
            "১৫-২০"
        )
        val indexOfselectedItem = knownToMerchant.indexOf(preselectedItem)
        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, knownToMerchant)
        binding?.spinneraverKnownToMerchnat?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.spinneraverKnownToMerchnat?.setSelection(indexOfselectedItem)
        }
        binding?.spinneraverKnownToMerchnat?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedKnownMerchantDuration = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun setUpSpinnerAverageOrderSpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val averageOrder: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "০-৫০০",
            "৫০০-১০০০",
            "১০০০-১৫০০",
            "১৫০০-২০০০",
            "২০০০-৩০০০",
            "৩০০০-৪০০০"
        )
        val indexOfselectedItem = averageOrder.indexOf(preselectedItem)
        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, averageOrder)
        binding?.AverageOrderToMerchnat?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.AverageOrderToMerchnat?.setSelection(indexOfselectedItem)
        }
        binding?.AverageOrderToMerchnat?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedAverageOrder = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun setUpSpinnerMonthlyExpSpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val MonthlyExp: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "০-৫,০০০",
            "৫,০০০-১০,০০০",
            "১০,০০০-১৫,০০০",
            "১৫,০০০-২০,০০০",
            "২০,০০০-২৫,০০০",
            "২৫,০০০-৩০,০০০",
            "৩০,০০০-৪০,০০০",
            "৪০,০০০-৬০,০০০",
            "৬০,০০০-১,০০,০০০"
        )

        val indexOfselectedItem = MonthlyExp.indexOf(preselectedItem)
        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, MonthlyExp)
        binding?.spinnerMonthlyExpType?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.spinnerMonthlyExpType?.setSelection(indexOfselectedItem)
        }
        binding?.spinnerMonthlyExpType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedMonthlyExp = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun setUpSpinnerCurrentLoanEMISpinner(preselectedItem: String = "", hasPreviousSelection: Boolean = false) {

        val CurrentLoanEMI: MutableList<String> = mutableListOf(
            "বেছে নিন",
            "৫০০-১০০০",
            "১০০০-২০০০",
            "২০০০-৪০০০",
            "৪০০০-৬০০০",
            "৬০০০-৮০০০",
            "৮০০০-১০০০০",
            "১০০০০-১২০০০",
            "১২০০০-১৫০০০",
            "১৫০০০-২০০০০"
        )
        val indexOfselectedItem = CurrentLoanEMI.indexOf(preselectedItem)
        val spinnerAdapter =
            CustomSpinnerAdapter(requireContext(), R.layout.item_view_spinner_item, CurrentLoanEMI)
        binding?.spinnerCurrentLoanEMIType?.adapter = spinnerAdapter
        if (indexOfselectedItem > 0) {
            binding?.spinnerCurrentLoanEMIType?.setSelection(indexOfselectedItem)
        }
        binding?.spinnerCurrentLoanEMIType?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCurrentLoanEMI = if (position > 0) {
                        spinnerAdapter.getItem(position)!!
                    } else {
                        ""
                    }
                }
            }
    }

    private fun datePickerTradeLisence() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select trade license ")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            setDateRangePickerTitleTradeLisencee(it)
        }
    }

    private fun setDateRangePickerTitleTradeLisencee(selectedDOB: Long, fromInit: Boolean = false, serverDate: String = "") {
        if (fromInit) {
            selectedDateTradeLisence = DigitConverter.formatDate(serverDate, "yyyy-MM-dd", "yyyy-MM-dd")
            selectedDateFormattedTradeLisence = DigitConverter.formatDate(serverDate, "yyyy-MM-dd", "dd MMM yyyy")
            binding?.tradeLicenseExpireDateTV?.setText(selectedDateFormattedTradeLisence)
            isExpireDateofTradeLisenseSelectd = true
        } else {
            selectedDateTradeLisence = sdf.format(selectedDOB)
            selectedDateFormattedTradeLisence = sdf1.format(selectedDOB)
            binding?.tradeLicenseExpireDateTV?.setText(selectedDateFormattedTradeLisence)
            isExpireDateofTradeLisenseSelectd = true
        }
    }

    private fun datePickerDOB() {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTheme(R.style.CustomMaterialCalendarTheme)
        builder.setTitleText("Select your birthday")
        val picker = builder.build()
        picker.show(childFragmentManager, "Picker")
        picker.addOnPositiveButtonClickListener {
            setDateRangePickerTitleDOB(it)
        }
    }

    private fun setDateRangePickerTitleDOB(selectedDOB: Long, fromInit: Boolean = false, serverDate: String = "") {
        if (fromInit) {
            selectedDateDOB = DigitConverter.formatDate(serverDate, "yyyy-MM-dd", "yyyy-MM-dd")
            selectedDateFormattedDOB = DigitConverter.formatDate(serverDate, "yyyy-MM-dd", "dd MMM yyyy")
            binding?.DOBET?.setText(selectedDateFormattedDOB)
        } else {
            selectedDateDOB = sdf.format(selectedDOB)
            selectedDateFormattedDOB = sdf1.format(selectedDOB)
            binding?.DOBET?.setText(selectedDateFormattedDOB)
        }
    }

    private fun fetchCourierList() {
        courierList.clear()
        viewModel.fetchCourierList().observe(viewLifecycleOwner, Observer { list ->
            if (!list.isNullOrEmpty()) {
                courierList.addAll(list)
                dataAdapter.initLoad(courierList)
            }
        })
    }

    private fun verify(): Boolean {

        if (merchantGender.isEmpty()) {
            context?.toast("লিঙ্গ নির্বাচন করুন")
            return false
        }

        merchantName = binding?.merchantNameET?.text.toString()
        if (merchantName.isEmpty()) {
            context?.toast("আপনার নাম লিখুন")
            return false
        }

        if (adapterAge.selectedItem == "") {
            context?.toast("আপনার বয়স নির্বাচন করুন")
            return false
        }

        nidCardNo = binding?.nidCardNoET?.text.toString()
        if (nidCardNo.isEmpty()) {
            context?.toast("আপনার এনআইডি নাম্বার উল্লেখ করুন")
            return false
        } else if (nidCardNo.length <= 9) {
            context?.toast("আপনার সঠিক এনআইডি নাম্বার উল্লেখ করুন")
            return false
        }

        if (binding?.DOBET?.text.toString().isEmpty()) {
            context?.toast("আপনার আপনার জন্মতারিখ উল্লেখ করুন")
            return false
        }

        if (selectedEducation.isEmpty()) {
            context?.toast("শিক্ষাগত যোগ্যতা নির্বাচন করুন")
            return false
        }

        if (familyMemNumAdapter.selectedItem == "") {
            context?.toast("পরিবারের সদস্য সংখ্যা লিখুন")
            return false
        }

        if (houseOwnerAdapter.selectedItem == "") {
            context?.toast("আপনার বাসার মালিকানা তথ্য নির্বাচন করুন")
            return false
        }

        if (locationAdapter.selectedItem == "") {
            context?.toast("আপনার বাসার অবস্থান নির্বাচন করুন")
            return false
        }

        if (marriageStatusAdapter.selectedItem == "") {
            context?.toast("বৈবাহিক অবস্থা নির্বাচন করুন")
            return false
        }

        if (selectedAverageBasket.isEmpty()) {
            context?.toast("আপনার গড় বাস্কেট তথ্য নির্বাচন করুন")
            return false
        }

        if (selectedMonthlyExp.isEmpty()) {
            context?.toast("আপনার মাসিক ব্যায়ের তথ্য দিন")
            return false
        }

        if (selectedKnownMerchantDuration.isEmpty()) {
            context?.toast("আপনার মার্চেন্ট এর সাথে পরিচয়ের সময়কাল নির্বাচন করুন")
            return false
        }

        loanRange = binding?.loanRangeET?.text.toString() ?: ""
        if (loanRange.isEmpty()) {
            context?.toast("আপনার কাঙ্ক্ষিত লোন রেঞ্জ লিখুন")
            return false
        }

        loanRepayMonthPeriod = binding?.reqTenorMonthET?.text.toString() ?: ""
        if (loanRepayMonthPeriod.isEmpty()) {
            context?.toast("আপনি কত মাসের মধ্যে লোন পরিশোধ করতে ইচ্ছুক")
            return false
        } else if (loanRepayMonthPeriod.toInt() >= 60) {
            context?.toast("লোন পরিশোদের সময়সূচি ৬০ মাসের মধ্যে হতে হবে")
            return false
        } else if (loanRepayMonthPeriod.toInt() <= 0) {
            context?.toast("লোন পরিশোদের সময়সূচি শুন্য মাসের সমান বা কম হতে পারে না")
            return false
        }

        yearlyTotalIncome = binding?.yearlyTotalIncomeET?.text.toString().toDouble().toInt() ?: 0
        if (yearlyTotalIncome <= 0) {
            context?.toast("আপনার বাৎসরিক সর্বমোট আয় শুন্য বা তার কম হতে পারে না")
            return false
        }
        otherIncome = binding?.otherIncomeET?.text.toString() ?: ""
        if (otherIncome.isEmpty()) {
            context?.toast("অন্যান্য উৎস থেকে সর্বমোট আয় উল্লেখ করুন")
            return false
        } else if (otherIncome.toDouble().toInt() ?: 0 <= 0) {
            context?.toast("অন্যান্য উৎস থেকে সর্বমোট আয় শুন্য বা তার কম হতে পারে না")
            return false
        }

        monthlyTransaction = binding?.monthlyFBTransactionET?.text.toString() ?: ""
        if (monthlyTransaction.isEmpty()) {
            context?.toast("ফেসবুকের মাধ্যমে মাসে কত বিক্রি লিখুন")
            return false
        } else if (monthlyTransaction.toDouble().toInt() ?: 0 <= 0) {
            context?.toast("ফেসবুকের মাধ্যমে মাসে আয় শুন্য বা তার কম হতে পারে না")
            return false
        }


        if (binding?.merchantPhysicalShopExistsRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("ফিজিকাল দোকানের তথ্য দিন")
            return false
        }

        if (hasPhysicalShop) {
            totalMonthlyAverageSell = binding?.totalMonthlyAverageSellET?.text.toString()
            if (totalMonthlyAverageSell.isEmpty()) {
                context?.toast("আপনার গড় মাসিক বিক্রির তথ্য লিখুন")
                return false
            } else if (totalMonthlyAverageSell.toDouble().toInt() ?: 0 <= 0) {
                context?.toast("গড় মাসিক বিক্রি শুন্য বা তার কম হতে পারে না")
                return false
            }
        } else {
            totalMonthlyAverageSell = ""
        }

        if (binding?.shopOwnershipInBusinessRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("আপনার ব্যবসার মালিকানা নির্বাচন করুন")
            return false
        }


        totalMonthlyCOD = binding?.totalCODFromOtherServicesET?.text.toString() ?: ""
        if (totalMonthlyCOD.isEmpty()) {
            context?.toast("আপনার টোটাল মাসিক COD দিন")
            return false
        } else if (totalMonthlyCOD.toDouble().toInt() ?: 0 <= 0) {
            context?.toast("আপনার টোটাল মাসিক COD শুন্য বা তার কম হতে পারে না")
            return false
        }

        if (dataAdapter.getSelectedItemModelList().isEmpty()) {
            context?.toast("অন্য যে কুরিয়ার সার্ভিস সেবা গ্রহণ করেন তা নির্বাচন করুন")
            return false
        }

        if (binding?.merchantTakeLoanRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("পূর্বের লোনের তথ্য দিন")
            return false
        }

        if (hasPreviousLoan) {
            if (binding?.loanAMountET?.text.isNullOrBlank()) {
                context?.toast("পূর্বের লোনের পরিমাণ লিখুন")
                return false
            } else if (binding?.loanAMountET?.text.toString().toDouble().toInt() ?: 0 < 1) {
                context?.toast("পূর্বের লোনের সঠিক এমাউন্ট উল্লেখ করুন")
                return false
            } else {
                previousTakingLoanAmount = binding?.loanAMountET?.text.toString().toDouble().toInt() ?: 0
            }
            if (binding?.bankNameET?.text!!.isEmpty()) {
                context?.toast("লোনের দাতা ব্যাংকের নাম লিখুন")
                return false
            } else {
                bankName = binding?.bankNameET?.text.toString() ?: ""
            }

            if (binding?.loanRepayRadioGroupType?.checkedRadioButtonId == -1) {
                context?.toast("আপনার লোন পরিশোধের ধরণ নির্বাচন করুন")
                return false
            }
            if (selectedCurrentLoanEMI.isEmpty()) {
                context?.toast("আপনার লোন ই এম আই এর পরিমান দিন")
                return false
            }

        } else {
            previousTakingLoanAmount = 0
            bankName = ""
        }

        if (binding?.merchantHasBankAccountRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("পূর্বের কোনো ব্যাংক একাউন্ট তথ্য যদি থাকে তবে তা নির্বাচন করুন")
            return false
        }
        if (binding?.merchantHasBankAccountRadioGroup?.checkedRadioButtonId == R.id.merchantHasBankAccountYes
            && (binding?.bankAccountNumberET?.text.toString().isEmpty())
        ) {
            context?.toast("পূর্বের কোনো ব্যাংক একাউন্ট তথ্য যদি থাকে তবে তা নির্বাচন করুন")
            return false
        } else if (binding?.merchantHasBankAccountRadioGroup?.checkedRadioButtonId == R.id.merchantHasBankAccountYes
            && binding?.companyBankNameTextInput?.text.toString().isEmpty()
        ) {
            context?.toast("পূর্বের কোনো ব্যাংকের নাম লিখুন")
            return false
        }

        if (binding?.haveAnyCreditCardRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("আপনার কার্ডের তথ্য দিন")
            return false
        }

        if (hasCreditCard) {
            if (binding?.creditCardName?.text.toString().isEmpty()) {
                context?.toast("ক্রেডিট কার্ডের ব্যাংকের নাম লিখুন")
                return false
            }
            if (binding?.creditCardLimit?.text.toString().isEmpty()) {
                context?.toast("ক্রেডিট কার্ডের লিমিট দিন")
                return false
            }
        }

        if (binding?.haveAnyTINRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("আপনার TIN তথ্য দিন")
            return false
        }

        if (hasTin) {
            if (binding?.teamTINNumberET?.text.toString().isEmpty()) {
                context?.toast("আপনার TIN নাম্বার দিন")
                return false
            } else if ((binding?.teamTINNumberET?.text.toString().trim().length < 12)) {
                context?.toast("আপনার ১২ ডিজিটের সঠিক TIN নাম্বার দিন")
                return false
            }
        }

        if (binding?.merchantHasTradeLicenceRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("ট্রেড লাইসেন্স এর তথ্য দিন")
            return false
        }
        if (binding?.merchantHasTradeLicenceRadioGroup?.checkedRadioButtonId == R.id.merchantHasTradeLicenceYes
            && binding?.tradeLicenseNoTV?.text.toString().isEmpty()
        ) {
            context?.toast("ট্রেড লাইসেন্স থাকলে নাম্বার দেয়া বাধতামূলক")
            return false
        } else if (binding?.merchantHasTradeLicenceRadioGroup?.checkedRadioButtonId == R.id.merchantHasTradeLicenceYes
            && binding?.tradeLicenseExpireDateTV?.text.toString().isEmpty()
        ) {
            context?.toast("ট্রেড লাইসেন্স এর এক্সপায়ার ডেট ইনপুট দিন")
            return false
        } else if (binding?.merchantHasTradeLicenceRadioGroup?.checkedRadioButtonId == R.id.merchantHasTradeLicenceYes
            && binding?.tradeLicenseNoTV?.text.toString().length < 9
        ) {
            context?.toast("ট্রেড লাইসেন্স এর সঠিক নাম্বার প্রবেশ করুন")
            return false
        }
        if (imagePickFlag == 1) {
            if (tradeLicenseImageUrl.isEmpty()) {
                context?.toast("ট্রেড লাইসেন্স এর ছবি অ্যাড করুন")
                return false
            }
        }
        /*if (!isExpireDateofTradeLisenseSelectd) {
            context?.toast("ট্রেড লাইসেন্স এর এক্সপায়ার ডেট এড করুন")
            return false
        }*/


        if (binding?.merchantHasGuarantorRadioGroup?.checkedRadioButtonId == -1) {
            context?.toast("গ্যারান্টার এর তথ্য দিন")
            return false
        }

        guarantorName = binding?.merchantGuarantorNameET?.text.toString()
        guarantorNumber = binding?.merchantGuarantorNumberET?.text.toString()
        if (hasGuarantor) {
            if (guarantorName.isEmpty()) {
                context?.toast("গ্যারান্টার এর নাম লিখুন")
                return false
            } else if (guarantorNumber.isEmpty()) {
                context?.toast("গ্যারান্টার এর মোবাইল নাম্বার লিখুন")
                return false
            } else if (guarantorNumber.length != 11 || !isValidMobileNumber(guarantorNumber)) {
                context?.toast("গ্যারান্টার এর সঠিক মোবাইল নাম্বার লিখুন")
                return false
            }
        }

        if (selectedAverageOrder.isEmpty()) {
            context?.toast("আপনার গড় অর্ডার এর তথ্য দিন")
            return false
        }
        if (selectedMonthlyExp.isEmpty()) {
            context?.toast("আপনার মাসিক ব্যায় এর পরিমান দিন")
            return false
        }
        if (hasPreviousData && fetchedToken.isEmpty()) {
            context?.toast("দুঃখিত এ মুহূর্তে আপডেটে সমস্যা হচ্ছে, একটু পর আবার চেষ্টা করুন")
            return false
        }

        return true
    }
}