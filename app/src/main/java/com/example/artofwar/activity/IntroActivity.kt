package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.artofwar.R

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        var handler= Handler()
        handler.postDelayed({
            val intent= Intent(this,LoginActivity::class.java)
            startActivity(intent)
        },1500)
    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}