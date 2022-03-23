package com.example.netotlogyview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<StateView>(R.id.stats).data = listOf(
            500F,
            500F,
            500F,
            500F,
        )
    }

}