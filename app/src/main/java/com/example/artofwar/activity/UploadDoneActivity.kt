package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_upload_done.*

class UploadDoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_done)

        val access_token=intent.getStringExtra("access_token")

        bt_goHome.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}