package com.example.playandroidwork.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.playandroidwork.R
import com.example.playandroidwork.mvptest.TestActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn1.setOnClickListener(View.OnClickListener {
//            startActivity(Intent(this,
//                NetActivity::class.java))

            startActivity(Intent(this,
                GradientRoundActivity::class.java))
        })

        btn2.setOnClickListener {
            startActivity(Intent(this,
                TestActivity::class.java))
        }

        btn3.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    DemoActivity::class.java
                )
            )
        }

    }
}
