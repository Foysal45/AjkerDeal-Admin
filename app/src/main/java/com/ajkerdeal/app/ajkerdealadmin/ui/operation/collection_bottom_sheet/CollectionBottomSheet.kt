package com.ajkerdeal.app.ajkerdealadmin.ui.operation.collection_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentCollectionBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.isValidDTCode
import com.ajkerdeal.app.ajkerdealadmin.utils.isValidDTCodeChat
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.Exception
import kotlin.concurrent.thread

class CollectionBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentCollectionBottomSheetBinding? = null
    var onOrderCollectClicked: ((orderId: String) -> Unit)? = null
    var onProductRecievedClicked: ((orderId: String) -> Unit)? = null
    var isProductRecieved: Boolean = false
    var shouldAppendDT: Boolean = false


    companion object {
        fun newInstance(isProductRecieved: Boolean = false): CollectionBottomSheet = CollectionBottomSheet().apply {
            this.isProductRecieved = isProductRecieved
        }
        val tag: String = CollectionBottomSheet::class.java.name
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
        return FragmentCollectionBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(isProductRecieved){
            binding?.updateStatus?.text = "Receive Parcel"
        }else{
            binding?.updateStatus?.text = "Collect Parcel"
        }

        binding?.updateStatus?.setOnClickListener {
            hideKeyboard()
            if (validate()) {
                if(isProductRecieved){
                    onProductRecievedClicked?.invoke( if(shouldAppendDT) "DT-${binding?.etOrderId?.text.toString()}" else binding?.etOrderId?.text.toString())
                }else{
                    onOrderCollectClicked?.invoke(binding?.etOrderId?.text.toString())
                }

            }
        }
    }

    private fun validate(): Boolean {

        val orderId = binding?.etOrderId?.text.toString()

        if (orderId.trim().isEmpty()) {
            context?.toast("Please, Write Order ID")
            return false
        }
        try {
            if (orderId.toInt() > 0) {
                shouldAppendDT = true
                return true
            }
        }catch (ex: Exception){

        }
        if (!isValidDTCode(orderId)) {
            context?.toast("Please, Write valid Order ID")
            return false
        }

        return true
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}