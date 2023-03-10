package com.ajkerdeal.app.ajkerdealadmin.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ajkerdeal.app.ajkerdealadmin.BuildConfig
import com.ajkerdeal.app.ajkerdealadmin.api.models.login.LoginRequest
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentLoginBinding
import com.ajkerdeal.app.ajkerdealadmin.utils.SessionManager
import com.ajkerdeal.app.ajkerdealadmin.utils.hideKeyboard
import com.ajkerdeal.app.ajkerdealadmin.utils.toast
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by inject()

    companion object {
        fun newInstance() : LoginFragment = LoginFragment().apply{}
        val tag: String = LoginFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLoginBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            binding?.etUserId?.setText("admin")
            binding?.etLoginPassword?.setText("123456")
        }

        binding?.loginBtn?.setOnClickListener {
            login()
        }
    }

    private fun login() {
        hideKeyboard()
        val userId = binding?.etUserId?.text.toString().trim()
        val password =  binding?.etLoginPassword?.text.toString().trim()
        if (userId.isEmpty() || password.isEmpty()){
            context?.toast("User আইডি অথবা পাসওয়ার্ড দিন")
        }else{
            val requestBody = LoginRequest(userId, password, SessionManager.deviceId, SessionManager.firebaseToken)
            viewModel.employeeLogin(requestBody).observe(viewLifecycleOwner, Observer { model->
                if (model.status){
                    SessionManager.createSession(model.body)
                    if (activity != null) {
                        (activity as LoginActivity).goToHome()
                    }
                }else{
                    context?.toast("User আইডি অথবা পাসওয়ার্ড সঠিক নয়")
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}