package com.zadanierekrutacyjne2

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class ItemActivity : AppCompatActivity() {
    var webDetail: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        webDetail = findViewById(R.id.webDetail)
        val intent = intent
        val http = intent.getStringExtra("http")
        webDetail!!.loadUrl(http!!)
        val webSettings = webDetail!!.getSettings()
        webSettings.javaScriptEnabled = true
        webSettings.setSupportMultipleWindows(true)

        webDetail!!.setWebChromeClient(object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                window.setTitle(title)
            }
        })

        webDetail!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false
            }
        })
    }
}