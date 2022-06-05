package com.example.thirdpartylibdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        iv_simple_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.image))
        iv_circular_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.image))
    }
}