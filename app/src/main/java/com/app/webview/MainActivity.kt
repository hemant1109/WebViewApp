package com.app.webview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings.LOAD_NO_CACHE
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.app.webview.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private var isReload: Boolean = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cirProgressBar.isVisible = true
        binding.webview.settings.apply {
            javaScriptEnabled = true
            // no cache
            cacheMode = LOAD_NO_CACHE
            //end
        }
        // no cookie
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(false)
        //end
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
                Log.e("webViewClientStarted", "webViewClient $url")
                binding.cirProgressBar.isVisible = true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("webViewClientFinishe", "webViewClient $url")
                binding.cirProgressBar.isVisible = false
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                super.doUpdateVisitedHistory(view, url, isReload)
                Log.e("webViewClienHistory", "webViewClient $url")
                Log.e("webViewClienHistory", "webViewClient $isReload")
                this@MainActivity.isReload = isReload
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (isReload) {
                        return false
                    }
                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(it))
                    startActivity(browserIntent)
                    Log.e("webViewClient", "webViewClient $it")
                }
                return true
            }
        }
        var url = BuildConfig.BASE_URL
        if (!BuildConfig.BASE_URL.startsWith("http")) {
            url = "https://${BuildConfig.BASE_URL}"
        }
        binding.webview.loadUrl(url)
    }
}