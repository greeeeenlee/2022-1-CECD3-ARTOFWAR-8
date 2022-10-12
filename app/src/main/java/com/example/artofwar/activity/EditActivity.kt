package com.example.artofwar.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        //TODO : DB에서 아이디,이름 받아오기


        bt_cancel.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        bt_modify.setOnClickListener {
            val pw1=et_pw.text.toString()
            val pw2=et_pw2.text.toString()

            if(pw1.isEmpty() || pw2.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(1)
            }
            else if(pw1!=pw2){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(2)
            }
            else{
                val intent=Intent(this,EditDoneActivity::class.java)
                startActivity(intent)
            }


        }
    }
}