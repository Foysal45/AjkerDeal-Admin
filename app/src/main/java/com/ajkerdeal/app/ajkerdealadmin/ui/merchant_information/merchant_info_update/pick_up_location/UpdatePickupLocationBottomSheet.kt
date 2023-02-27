package com.ajkerdeal.app.ajkerdealadmin.ui.merchant_information.merchant_info_update.pick_up_location


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.api.models.pickup_location.MerchantPickupLocation
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentUpdatePickupLocationBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.profile.ProfileViewModel
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.concurrent.thread

@SuppressLint("SetTextI18n")
class UpdatePickupLocationBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentUpdatePickupLocationBinding? = null
    var onUpdateClicked: ((model: MerchantPickupLocation) -> Unit)? = null

    private val viewModel: ProfileViewModel by inject()

    private lateinit var pickUpLocation: MerchantPickupLocation

    companion object {
        fun newInstance(pickUpLocation: MerchantPickupLocation): UpdatePickupLocationBottomSheet = UpdatePickupLocationBottomSheet().apply {
            this.pickUpLocation = pickUpLocation
        }

        val tag: String = UpdatePickupLocationBottomSheet::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()

        val dialog: BottomSheetDialog? = dialog as BottomSheetDialog?
        dialog?.setCanceledOnTouchOutside(true)
        val bottomSheet: FrameLayout? = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)

        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            thread {
                activity?.runOnUiThread {
                    val dynamicHeight = binding?.parent?.height ?: 500
                    BottomSheetBehavior.from(bottomSheet).peekHeight = dynamicHeight
                }
            }
            with(BottomSheetBehavior.from(bottomSheet)) {
                skipCollapsed = false
                isHideable = false

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentUpdatePickupLocationBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.districtSelect?.setText(pickUpLocation.districtName)
        binding?.thanaSelect?.setText(pickUpLocation.thanaName)
        binding?.pickupAddress?.setText(pickUpLocation.pickupAddress)

        binding?.pickupContact?.setText(pickUpLocation.mobile)

        binding?.updatePickupBtn?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                updatePickUpAddress()
            }
        }
    }

    private fun updatePickUpAddress() {
        viewModel.updatePickupLocations(pickUpLocation).observe(viewLifecycleOwner, Observer { model ->
            onUpdateClicked?.invoke(model)
        })
    }


    private fun validate(): Boolean {
        if (pickUpLocation.districtId == 0) {
            context?.toast(getString(R.string.select_dist))
            return false
        }
        if (pickUpLocation.thanaId == 0) {
            context?.toast(getString(R.string.select_thana))
            return false
        }

        val newPickUpAddress = binding?.pickupAddress?.text?.toString() ?: ""
        if (newPickUpAddress.trim().isEmpty() || newPickUpAddress.length < 15) {
            context?.toast("বিস্তারিত ঠিকানা লিখুন, ন্যূনতম ১৫ ডিজিট")
            binding?.pickupAddress?.requestFocus()
            return false
        }
        pickUpLocation.pickupAddress = newPickUpAddress

        val pickupContact = binding?.pickupContact?.text?.toString() ?: ""
        if (pickupContact.length != 11) {
            context?.toast("সঠিক মোবাইল নাম্বার লিখুন")
            binding?.pickupContact?.requestFocus()
            return false
        }
//        pickUpLocation.mobile = pickupContact

        return true
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}