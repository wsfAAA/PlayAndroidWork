package com.example.playandroidwork.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playandroidwork.R
import kotlinx.android.synthetic.main.activity_gradient_round.*

class GradientRoundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_round)

        tv_bgColor.setBgColor(Color.parseColor("#6f4df0"))

        tv_radius.setBgColor(Color.parseColor("#6f4df0"))
        tv_radius.setRadius(10)

        tv_leftTopRadius.setBgColor(Color.parseColor("#6f4df0"))
        tv_leftTopRadius.setRadius(30, 0, 0, 30)

        tv_bgGradient.setBgGradient(270, Color.parseColor("#6f4df0"), Color.parseColor("#000000"))
        tv_bgGradient.setRadius(10)

        tv_border.setBorder(Color.parseColor("#000000"),2)
        tv_border.setRadius(10)
    }
}