package com.ajkerdeal.app.ajkerdealadmin.ui.home.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.ajkerdeal.app.ajkerdealadmin.R
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentWorkUpdateBottomSheetBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.concurrent.thread


class WorkUpdateBottomSheet : BottomSheetDialogFragment() {

    private var binding: FragmentWorkUpdateBottomSheetBinding? = null
    var onWOrkUpdateClicked: ((report: String) -> Unit)? = null

    companion object {
        fun newInstance(): WorkUpdateBottomSheet = WorkUpdateBottomSheet().apply {}
        val tag: String = WorkUpdateBottomSheet::class.java.name
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
        return FragmentWorkUpdateBottomSheetBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.takingBreakBtn?.setOnClickListener {
            if (binding?.workUpdateTV?.text.isNullOrEmpty()){
                context?.toast("Please insert your Work Update")
            }else{
                hideKeyboard()
                onWOrkUpdateClicked?.invoke(binding?.workUpdateTV?.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}