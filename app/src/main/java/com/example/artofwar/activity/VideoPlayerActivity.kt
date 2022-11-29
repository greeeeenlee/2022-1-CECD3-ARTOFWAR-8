package com.example.artofwar.activity

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.MediaController
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)

        imgbtn_category.setOnClickListener {
            finish()
        }

        val video_address=intent.getStringExtra("video_storage_url")

        val videoUri= Uri.parse(video_address)
        val mediaController=MediaController(this)
        mediaController.setAnchorView(vv_player)

        vv_player.setMediaController(mediaController)
        vv_player.setVideoURI(videoUri)
        vv_player.start()

    }
}