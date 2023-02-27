package com.ajkerdeal.app.ajkerdealadmin.ui.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.ajkerdeal.app.ajkerdealadmin.databinding.FragmentWebViewBinding
import com.ajkerdeal.app.ajkerdealadmin.ui.home.HomeActivity
import org.koin.android.ext.android.inject
import timber.log.Timber

class WebViewFragment : Fragment() {

    //private val repository: AppRepository by inject()
    private var binding: FragmentWebViewBinding? = null

    private var webTitle: String = ""
    private var loadUrl: String = ""
    private var bundle: Bundle? = null

    /*companion object {
        fun newInstance(url: String, title: String, bundle: Bundle? = null): WebViewFragment = WebViewFragment().apply {
            this.loadUrl = url
            this.webTitle = title
            this.bundle = bundle
        }
        val tag: String = WebViewFragment::class.java.name
    }*/

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentWebViewBinding.inflate(inflater).also {
            binding = it
        }.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("WebView Url: $loadUrl")
        Timber.d("WebView Bundle: ${bundle.toString()}")

        //https://www.facebook.com/greenheavensj/videos/4027495200677685
        //https://www.facebook.com/206785353368499/videos/928460757730318
        this.loadUrl = arguments?.getString("loadUrl") ?: "https://m.ajkerdeal.com/play/livevideo.html?profile=206785353368499&video=928460757730318"//
        this.webTitle = arguments?.getString("webTitle") ?: ""

        binding?.webView!!.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            //loadWithOverviewMode = true
            //useWideViewPort = true
        }
        with(binding?.webView!!) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            clearHistory()
            isHorizontalScrollBarEnabled = false
            isVerticalScrollBarEnabled = false
            //addJavascriptInterface(WebAppInterface(requireContext(), repository, bundle), "Android")
            webViewClient = Callback()
            //clearCache(true)
        }

        binding?.webView?.loadUrl(loadUrl)
    }

    inner class Callback : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            //return super.shouldOverrideUrlLoading(view, request)
            val uri = request?.url
            val url = uri?.toString() ?: ""
            Timber.d("shouldOverrideUrlLoading intent $url")
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false
            } else if (url.startsWith("intent://")) {
                try {
                    val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                    if(intent != null){
                        Timber.d("shouldOverrideUrlLoading data ${intent.data}")
                        val packageManager = requireContext().packageManager
                        val info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                        if (info != null) {
                            Timber.d("shouldOverrideUrlLoading info ${info.activityInfo.packageName}")
                            requireContext().startActivity(intent)
                        }
                        val fallbackUrl = intent.getStringExtra("browser_fallback_url")
                        Timber.d("shouldOverrideUrlLoading fallbackUrl $fallbackUrl")
                        return if (fallbackUrl != null){
                            view?.loadUrl(fallbackUrl)
                            true
                        } else {
                            false
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding?.progressBar?.visibility = View.VISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            binding?.progressBar?.visibility = View.GONE
        }

        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
            Timber.d(error.toString())
            binding?.progressBar?.visibility = View.GONE
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            super.onReceivedSslError(view, handler, error)

            handler?.proceed()

            /*val builder = AlertDialog.Builder(requireContext())
            var message = when (error?.primaryError) {
                SslError.SSL_UNTRUSTED -> "The certificate authority is not trusted."
                SslError.SSL_EXPIRED -> "The certificate has expired."
                SslError.SSL_IDMISMATCH -> "The certificate Hostname mismatch."
                SslError.SSL_NOTYETVALID -> "The certificate is not yet valid."
                else -> "SSL Error."
            }
            message += " Do you want to continue anyway?"

            builder.setTitle("SSL Certificate Error")
            builder.setMessage(message)
            builder.setPositiveButton("continue") { _, _ -> handler?.proceed() }
            builder.setNegativeButton("cancel") { _, _ -> handler?.cancel() }
            val dialog = builder.create()
            dialog.show()*/
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
