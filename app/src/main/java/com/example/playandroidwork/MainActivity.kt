package com.example.playandroidwork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.example.base.net.RxClient
import com.example.base.net.api.ApiService
import com.example.base.net.callback.RxCallBack
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,TestActivity::class.java))
        })
    }
}
