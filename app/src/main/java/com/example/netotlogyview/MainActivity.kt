package com.example.netotlogyview

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<StateView>(R.id.stats)
        view.data = listOf(
                500F,
                500F,
                500F,
                500F,
        )

        ObjectAnimator.ofFloat(view, View.ROTATION, 360F).apply {
            duration = 3000
            interpolator = LinearInterpolator()
        }.start()



            val viewAnim = AnimationUtils.loadAnimation(
                this, R.anim.view_animation
            )

            view.startAnimation(viewAnim)
        }
    }
