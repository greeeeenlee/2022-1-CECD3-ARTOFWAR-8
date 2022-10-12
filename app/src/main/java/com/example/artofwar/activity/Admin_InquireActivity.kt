package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_admin_inquire.*
import java.io.File
import java.lang.Exception

class Admin_InquireActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_inquire)

        //TODO : DB에서 사용자아이디 받아오기

        val inquire=intent.getSerializableExtra("InquireInfo") as UserInquire
        tv_inquire_id.text=inquire.uid.toString()
        tv_inquire_title.text=inquire.inquire_title
        tv_inquire_content.text=inquire.inquire_content


        //비디오 시청하기 버튼을 클릭하는 경우
        bt_video.setOnClickListener {
            //TODO : 비디오 시청하기 연결
            val awsCredentials= BasicAWSCredentials("accessKey","secretKey")
            val s3Client= AmazonS3Client(awsCredentials, Region.getRegion(Regions.US_WEST_1))

            val transferUtility= TransferUtility.builder().s3Client(s3Client).context(this).defaultBucket("aowbuket").build()
            TransferNetworkLossHandler.getInstance(this)

            var testile= File("C:\\Users\\dlchf\\artofwar\\app\\src\\main\\res\\drawable\\file_upload.png")

            Log.d("LOG_TEST",filesDir.absolutePath.toString())
            val uploadObserver=transferUtility.download("video_221009_142055.jpeg",File(filesDir.absolutePath+"/test.jpeg"))
//                val uploadObserver=transferUtility.upload("aowbuket/${videoFileName}",videofile,CannedAccessControlList.PublicRead)

            uploadObserver.setTransferListener(object: TransferListener {
                override fun onStateChanged(id: Int, state: TransferState?) {
                    if(state== TransferState.COMPLETED){
                        Log.d("LOG_test", "onStateChanged: " + id + ", " + state.toString());
                    }
                }

                override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    val percentDonef=(bytesCurrent/bytesTotal)*100
                    val percentDone=percentDonef.toInt()

                    Log.d("LOG_test","ID : "+id+"bytesCurrent : "+bytesCurrent+"bytesTotal : "+bytesTotal+"percentDone : "+percentDone+"%")
                }

                override fun onError(id: Int, ex: Exception?) {
                    Log.d("LOG_test",ex?.message.toString())
                }

            })





        }

        //취소하기 버튼을 클릭하는 경우
        bt_cancel.setOnClickListener {
            val intent=Intent(this,Admin_InquireListActivity::class.java)
            startActivity(intent)
        }

        //보내기 버튼을 클릭하는 경우
        bt_answer.setOnClickListener {
            val answer=et_answer.text.toString()
            if(answer.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(8)
            }
            else{
                val intent=Intent(this,Admin_InquireDoneActivity::class.java)
                startActivity(intent)
            }
        }
    }
}