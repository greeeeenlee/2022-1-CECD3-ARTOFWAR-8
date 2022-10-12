package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_inquire_done.*

class Admin_InquireDoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_inquire_done)

        bt_goHome.setOnClickListener {
            val intent= Intent(this,Admin_MainActivity::class.java)
            startActivity(intent)
        }
    }
}