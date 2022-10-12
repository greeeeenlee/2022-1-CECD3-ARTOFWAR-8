package com.example.artofwar.activity

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import com.iceteck.silicompressorr.SiliCompressor
import java.io.File

class CompressVideo(context:Context) : AsyncTask<String, String, String>() {

    val context=context

    override fun onPreExecute() {
        super.onPreExecute()
    }


    override fun doInBackground(vararg p0: String?): String {

        val uri= Uri.parse(p0[1])
        val videoPath=SiliCompressor.with(context).compressVideo(uri,p0[2])

        return videoPath

    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        val file=File(result)
        val uri=Uri.fromFile(file)

    }


}
