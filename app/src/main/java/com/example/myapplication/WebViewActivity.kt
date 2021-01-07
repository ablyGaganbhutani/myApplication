package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity


class WebViewActivity : AppCompatActivity() {
    private var webView: WebView? = null
    private var handler: Handler? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        init_webView()
        handler = Handler()
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init_webView() {
        webView?.settings?.javaScriptEnabled = true //javascript
        webView?.settings?.javaScriptCanOpenWindowsAutomatically = true // window.open
        webView?.addJavascriptInterface(AndroidBridge(), "android")
        webView?.webChromeClient = WebChromeClient()

        //(assets / www / index.html) to the web server and fetch the address of the corresponding web server.
        //(assets/www/index.html) 에 있는 파일을 웹 서버에 올려서 해당 웹서버의 주소를 가져 온다. 로컬에서는 동작하지 않으므로 반드시 웹에 올려서 테스트를 한다..
        webView?.loadUrl("https://URL.html")
    }

    private inner class AndroidBridge {
        @JavascriptInterface
        fun setAddress(arg1: String, arg2: String, arg3: String) {
            handler?.post(Runnable {
                Log.d("Args", "arg1 : $arg1, arg2 : $arg2, arg3 : $arg3")
                val intent = Intent()
                intent.putExtra("arg1", arg1)
                intent.putExtra("arg2", arg2)
                intent.putExtra("arg3", arg3)
                setResult(Activity.RESULT_OK, intent)
                finish()
            })
        }
    }
}