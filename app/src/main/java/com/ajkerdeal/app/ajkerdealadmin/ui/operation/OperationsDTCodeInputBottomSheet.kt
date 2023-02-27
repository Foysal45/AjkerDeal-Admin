package com.ajkerdeal.app.ajkerdealadmin.ui.operation

import android.os.Bundle
import android.widget.FrameLayout
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentCollectionBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.operation.collection_bottom_sheet.CollectionBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread

class OperationsDTCodeInputBottomSheet : BottomSheetDialogFragment() {
    private var binding: FragmentCollectionBottomSheetBinding? = null
    var onOrderClicked: ((model: String) -> Unit)? = null


    companion object {
        fun newInstance(): OperationsDTCodeInputBottomSheet = OperationsDTCodeInputBottomSheet().apply {}
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
        val bottomSheet: FrameLayout? = dialog?.findViewById(R.id.design_bottom_sheet)
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
}