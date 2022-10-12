
package com.example.artofwar.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_edit.*

class VideoEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_edit)

        bt_modify.setOnClickListener {
            val dialog=Alert_Modify(this)
            dialog.showDialog()
        }
    }
}