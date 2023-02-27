package com.ajkerdeal.app.ajkerdealadmin.ui.retention.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.RetentionMerchentListModel
import com.ajkerdeal.app.ajkerdealadmin.api.models.retention.visit_info.VisitedInfoRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentVisitInfoBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.retention.RetentionMerchantListViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

class VisitInfoBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentVisitInfoBottomSheetBinding? = null

    private val viewModel: RetentionMerchantListViewModel by inject()

    private lateinit var model: RetentionMerchentListModel

    companion object {
        fun newInstance(model: RetentionMerchentListModel): VisitInfoBottomSheet = VisitInfoBottomSheet().apply {
            this.model = model
        }
        val tag: String = VisitInfoBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        val dialog: BottomSheetDialog? = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(false)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parent?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            with(BottomSheetBehavior.from(bottomSheet)) {
                skipCollapsed = true
                isHideable = false
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentVisitInfoBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.gpsLocation?.setText("${SessionManager.latitude}, ${SessionManager.longitude}")
        binding?.gpsLocation?.isEnabled = false

        binding?.saveBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                val requestBody = VisitedInfoRequest(
                    model.courierUserId, SessionManager.dtUserId,
                    SessionManager.latitude, SessionManager.longitude,
                    binding?.visitSummary?.text.toString()
                )

                viewModel.updateVisitedMerchant(requestBody).observe(viewLifecycleOwner, Observer { model ->
                    if (model.merchantVisitedId > 0) {
                        dismiss()
                        context?.toast("Updated successfully")
                    } else {
                        context?.toast("Failed! Try again.")
                    }
                })

            }
        }
    }

    private fun validate(): Boolean {

        val visitSummary = binding?.visitSummary?.text.toString()

        if (SessionManager.dtUserId == 0) {
            context?.toast("User id not found!, try login again!")
            return false
        }

        if (visitSummary.trim().isEmpty()) {
            context?.toast("Please, Write Visited Summary")
            return false
        }

        return true
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}
