package com.ajkerdeal.app.ajkerdealadmin.di

import com.ajkerdeal.app.ajkerdealadmin.api.*
import com.ajkerdeal.app.ajkerdealadmin.api.RetrofitUtils.createCache
import com.ajkerdeal.app.ajkerdealadmin.api.RetrofitUtils.createOkHttpClient
import com.ajkerdeal.app.ajkerdealadmin.api.RetrofitUtils.getGson
import com.ajkerdeal.app.ajkerdealadmin.api.RetrofitUtils.retrofitInstance
import com.ajkerdeal.app.ajkerdealadmin.repository.AppRepository
import com.ajkerdeal.app.ajkerdealadmin.ui.attendance_report.AttendanceReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.chat.compose.ChatComposeViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.ComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.complain_history.ComplainHistoryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list.MerchantComplainViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.complain_dt.merchant_complain_list.complain_history.MerchantComplainHistoryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.contact_info.ContactInfoViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.employee_leave.LeaveApplicationViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivityViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.instant_payment_information.InstantPaymentViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.keyword_survey.KeyWordSurveyViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.leave_report.report.LeaveReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey.LoanSurveryViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.loan_survey_report.LoanSurveyReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.login.LoginViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.MerchantInfoViewModelList
import com.ajkerdeal.app.ajkerdealadmin.ui.operation.OperationViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.operation.operation_menu.OperationMenuViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.order_report.order.OrderReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.post_shipment_chat.PostShipmentChatViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.profile.ProfileViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.RetentionMerchantListViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.all_orders.AllOrderViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.quick_booking.QuickOrderRequestViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention_report.RetentionReportViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.retention_users_chat.RetentionUsersListViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.riders_chat.RidersViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.track_order.TrackOrderViewModel
import com.ajkerdeal.app.ajkerdealadmin.ui.telesales_active_merchant.TeleSalesViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.AppConstant
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single { getGson() }
    single { createCache(get()) }
    single { createOkHttpClient(get()) }
    single(named("normal")) { createOkHttpClient(get()) }

    single(named("ana")) { retrofitInstance(AppConstant.BASE_URL_ANA, get(), get(named("normal"))) }
    single(named("api")) { retrofitInstance(AppConstant.BASE_URL_API, get(), get()) }
    single(named("adcore")) { retrofitInstance(AppConstant.BASE_URL_ADCORE, get(), get()) }
    single(named("adm")) { retrofitInstance(AppConstant.BASE_URL_ADM, get(), get()) }
    single(named("fcm")) { retrofitInstance(AppConstant.BASE_URL_FCM, get(), get()) }

    single { ApiInterfaceAPI(get(named("api"))) }
    single { ApiInterfaceANA(get(named("ana"))) }
    single { ApiInterfaceCore(get(named("adcore"))) }
    single { ApiInterfaceADM(get(named("adm"))) }
    single { ApiInterfaceFCM(get(named("fcm"))) }

    single { AppRepository(get(), get(), get(), get(), get()) }

    single { HomeActivityViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { ComplainViewModel(get()) }
    viewModel { AttendanceReportViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { LeaveApplicationViewModel(get()) }
    viewModel { OrderReportViewModel(get()) }
    viewModel { LeaveReportViewModel(get()) }
    viewModel { KeyWordSurveyViewModel(get()) }
    viewModel { RetentionMerchantListViewModel(get()) }
    viewModel { OperationViewModel(get()) }
    viewModel { OperationMenuViewModel(get()) }
    viewModel { ComplainHistoryViewModel(get()) }
    viewModel { QuickOrderRequestViewModel(get()) }
    viewModel { ChatComposeViewModel(get()) }
    viewModel { RetentionReportViewModel(get()) }
    viewModel { TrackOrderViewModel(get()) }
    viewModel { TeleSalesViewModel(get()) }
    viewModel { InstantPaymentViewModel(get()) }
    viewModel { MerchantInfoViewModelList(get()) }
    viewModel { MerchantComplainViewModel(get()) }
    viewModel { LoanSurveryViewModel(get()) }
    viewModel { MerchantComplainHistoryViewModel(get()) }
    viewModel { RetentionUsersListViewModel(get()) }

    viewModel { RidersViewModel(get()) }
    viewModel { ContactInfoViewModel(get()) }
    viewModel { AllOrderViewModel(get()) }

    viewModel { LoanSurveyReportViewModel(get()) }
    viewModel { PostShipmentChatViewModel(get()) }
}