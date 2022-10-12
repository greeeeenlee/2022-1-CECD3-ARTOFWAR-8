package com.example.artofwar.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.parse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.lang.Exception
import retrofit2.Call
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream

import com.iceteck.silicompressorr.SiliCompressor


class UploadActivity : AppCompatActivity() {

    val REQUEST_READ_EXTERNAL_STORAGE=1000
    val OPEN_GALLERY=1
    val SELECT_IMAGE=10
    var imageFileName=""
    var videoFileName=""
    var imageFilePath=""
    var videoFilePath=""

    lateinit var videodatauri :Uri

    var big_category="nothing"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        //access_token
        val access_token=intent.getStringExtra("access_token")
        if (access_token != null) {
            Log.d("LOG",access_token)
        }

        //동영상 파일 선택 버튼을 클릭하는 경우
        bt_check.setOnClickListener {
            //권한이 거절된 상태
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                //사용자가 승인 거절을 누른 경우
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    val alert=AlertDialog.Builder(this)
                    alert.setTitle("권한 요청")
                        .setMessage("비디오를 업로드하려면 외부 저장소 접근이 필요합니다.")
                        .setPositiveButton("확인"){
                                dialog,which->
                            ActivityCompat.requestPermissions(this@UploadActivity,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_READ_EXTERNAL_STORAGE)
                        }
                        .show()
                }
                //사용자가 승인 거절과 동시에 다시 표시하지 않기 옵션을 선택한 경우
                //또는 ,아직 승인 요청을 한 적이 없는 경우
                else{
                    //ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_READ_EXTERNAL_STORAGE)
                    val intent=Intent()
                    intent.action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri=Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }
            }
            //권한이 승인된 상태
            else{
                openGallery_video()
            }
        }

        //비디오 섬네일 이미지 업로드 선택
        bt_check2.setOnClickListener {
            //권한이 거절된 상태
            if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                //사용자가 승인 거절을 누른 경우
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    val alert=AlertDialog.Builder(this)
                    alert.setTitle("권한 요청")
                        .setMessage("비디오를 업로드하려면 외부 저장소 접근이 필요합니다.")
                        .setPositiveButton("확인"){
                                dialog,which->
                            ActivityCompat.requestPermissions(this@UploadActivity,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_READ_EXTERNAL_STORAGE)
                        }
                        .show()
                }
                //사용자가 승인 거절과 동시에 다시 표시하지 않기 옵션을 선택한 경우
                //또는 ,아직 승인 요청을 한 적이 없는 경우
                else{
                    //ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_READ_EXTERNAL_STORAGE)
                    val intent=Intent()
                    intent.action=Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri=Uri.fromParts("package",packageName,null)
                    intent.data=uri
                    startActivity(intent)
                }
            }
            //권한이 승인된 상태
            else{
                openGallery_image()
            }
        }

        //카테고리 버튼을 클릭하는 경우
        imgbtn_category.setOnClickListener {
            val intent=Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        //대분류 라디오 그룹 설정
        rg_big_1.clearCheck()
        rg_big_1.setOnCheckedChangeListener(listener1())
        rg_big_2.clearCheck()
        //rg_big_2.setOnCheckedChangeListener(listener2())

        var small_category="nothing"
        rg_small.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb_small_info->small_category=rb_small_info.text.toString()
                R.id.rb_small_together->small_category=rb_small_together.text.toString()
            }
        }


        //취소 버튼을 클릭하는 경우
        bt_cancel.setOnClickListener {
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //업로드하기 버튼을 클릭하는 경우-모든 항목이 입력되었는지 확인해야함
        bt_register.setOnClickListener {
            val title=et_title.text.toString()
            val file_address=tv_file_address.text.toString()
            val image_address=tv_imagefile_address.text.toString()

            setbigcategory()

            Log.d(TAG,big_category)
            Log.d(TAG,small_category)

            if(title.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(1)
            }
            else if(big_category=="nothing" || small_category=="nothing"){

                val dialog=Alert_Dialog(this)
                dialog.showDialog(6)
            }
            else if(file_address.isEmpty()){
                val dialog=Alert_Dialog(this)
                dialog.showDialog(7)
            }
            else {//모두 입력한 경우
                //TODO : 넘기는 값 고려하기

                val videofile=File(videoFilePath)

//                val resolver=applicationContext.contentResolver
//                val inputstream=resolver.openInputStream(videodatauri)
//                val bitmap=BitmapFactory.decodeStream(inputstream)
//                val byteArrayOutputStream=ByteArrayOutputStream()
//                //val input=BufferedInputStream(applicationContext.contentResolver.openInputStream(videoFilePath.toUri()))
//                bitmap.compress(Bitmap.CompressFormat.JPEG,20,byteArrayOutputStream)
//                val requestBody= byteArrayOutputStream.toByteArray()
//                    .toRequestBody("image/jpeg".toMediaTypeOrNull(), 0)
//                val multipartBody=MultipartBody.Part.createFormData("videoFile",videoFileName,requestBody)
//                val videofilebody=videofile.absolutePath.toRequestBody(MultipartBody.FORM)
//                val videofilemultibody=MultipartBody.Part.createFormData("videoFile",videofile.name,videofilebody)



                val awsCredentials= BasicAWSCredentials("accessKey","secretKey")
                val s3Client= AmazonS3Client(awsCredentials, Region.getRegion(Regions.US_WEST_1))

                val transferUtility= TransferUtility.builder().s3Client(s3Client).context(this).defaultBucket("aowbuket").build()
                TransferNetworkLossHandler.getInstance(this)

                var testile=File("/data/user/0/com.example.artofwar/files/test.jpeg")

                val uploadObserver=transferUtility.upload("file_upload.png",testile)
//                val uploadObserver=transferUtility.upload("aowbuket/${videoFileName}",videofile,CannedAccessControlList.PublicRead)

                uploadObserver.setTransferListener(object: TransferListener{
                    override fun onStateChanged(id: Int, state: TransferState?) {
                        if(state==TransferState.COMPLETED){
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



//                val service=UploadRequest.RetrofitAPI.emgMedService
//                if(image_address.isEmpty()){//썸네일을 선택 안한 경우
//                    service.uploadVideo(access_token,multipartBody,null,title,big_category,small_category)
//                        .enqueue(object:retrofit2.Callback<UploadResponseBody>{
//                            override fun onResponse(
//                                call: Call<UploadResponseBody>,
//                                response: Response<UploadResponseBody>
//                            ) {
//                                Log.d("LOG_test_upload",response.toString())
//
//                                uploadresponse(response.code().toString(),access_token)
//                            }
//
//                            override fun onFailure(call: Call<UploadResponseBody>, t: Throwable) {
//                                Log.d("LOG_test_upload_fail",t.toString())
//                            }
//
//                        })
//                }
//                else{//썸네일을 선택한 경우
//                    val imagefile=File(imageFilePath)
//                    val imagefilebody= imagefile.absolutePath.toRequestBody(MultipartBody.FORM)
//                    val imagefilemultibody=MultipartBody.Part.createFormData("imageFile",imagefile.name,imagefilebody)
//                    service.uploadVideo(access_token,multipartBody,imagefilemultibody,title,big_category,small_category)
//                        .enqueue(object:retrofit2.Callback<UploadResponseBody>{
//                            override fun onResponse(
//                                call: Call<UploadResponseBody>,
//                                response: Response<UploadResponseBody>
//                            ) {
//                                Log.d("LOG_test_upload",response.toString())
//
//                                uploadresponse(response.code().toString(),access_token)
//                            }
//
//                            override fun onFailure(call: Call<UploadResponseBody>, t: Throwable) {
//                                Log.d("LOG_test_upload_fail",t.toString())
//                            }
//
//                        })
//                }
            }
        }

    }

    private fun setbigcategory() {
        if(rb_big_culture.isChecked)
            big_category=rb_big_culture.text.toString()
        else if(rb_big_leisure.isChecked)
            big_category=rb_big_leisure.text.toString()
        else if(rb_big_info.isChecked)
            big_category=rb_big_info.text.toString()
        else if(rb_big_exercise.isChecked)
            big_category=rb_big_exercise.text.toString()
        else if(rb_big_edu.isChecked)
            big_category=rb_big_edu.text.toString()
        else if(rb_big_other.isChecked)
            big_category=rb_big_other.text.toString()
        else
            big_category="nothing"
    }

    private fun uploadresponse(code:String, access_token:String?){
        when(code){
            "200"->{
                val intent=Intent(this@UploadActivity,UploadDoneActivity::class.java)
                intent.putExtra("access_token",access_token)
                startActivity(intent)
            }
            "400","401","402"->{
                val dialog=Alert_Dialog(this@UploadActivity)
                dialog.showDialog(13)
                val intent=Intent(this@UploadActivity,MainActivity::class.java)
                intent.putExtra("access_token",access_token)
                startActivity(intent)
            }
            "500"->{
                val dialog=Alert_Dialog(this@UploadActivity)
                dialog.showDialog(11)
                val intent=Intent(this@UploadActivity,MainActivity::class.java)
                intent.putExtra("access_token",access_token)
                startActivity(intent)
            }
        }
    }



    private fun openGallery_video() {
        val intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,OPEN_GALLERY)
    }

    private fun openGallery_image(){
        val intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        try {
            startActivityForResult(intent,SELECT_IMAGE)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //앨범 열기
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(requestCode==OPEN_GALLERY) {
                videodatauri=data?.data!!
                data?.data?.let { it->
                    contentResolver.query(it,null,null,null,null)}?.use {
                        cursor->
                    val nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE)
                    cursor.moveToFirst()
                    val name=cursor.getString(nameIndex)
                    videoFileName=name
                    val size=cursor.getLong(sizeIndex).toString()

                    tv_file_address.setText(name)
                    Log.d("video_size2",size)
                }
                videoFilePath=videodatauri.path.toString()


            }
            else if(requestCode==SELECT_IMAGE){
                data?.data?.let { it->
                    contentResolver.query(it,null,null,null,null)}?.use {
                        cursor->
                    val nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE)
                    cursor.moveToFirst()
                    val name=cursor.getString(nameIndex)
                    imageFileName=name
                    val size=cursor.getLong(sizeIndex).toString()
                    tv_imagefile_address.setText(name)
                    Log.d("image_size",size)
                }
                imageFilePath=data?.data?.path.toString()
                Log.d("imageFilePath",imageFilePath)
            }
            else{
                Log.d("D","동영상 업로드 실패")
            }
        }
    }

    //대분류 라디오 그룹 설정
    inner class listener1:RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            if (checkedId != -1) {
                rg_big_2.setOnCheckedChangeListener(null)
                rg_big_2.clearCheck()
                rg_big_2.setOnCheckedChangeListener(listener2())

//                when(checkedId){
//                    R.id.rb_big_culture->big_category=rb_big_culture.text.toString()
//                    R.id.rb_big_leisure->big_category=rb_big_leisure.text.toString()
//                    R.id.rb_big_info->big_category=rb_big_info.text.toString()
//                }

            }
        }

        inner class listener2 : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                if (checkedId != -1) {
                    rg_big_1.setOnCheckedChangeListener(null)
                    rg_big_1.clearCheck()
                    rg_big_1.setOnCheckedChangeListener(listener1())

//                    when(checkedId){
//                        R.id.rb_big_exercise->big_category=rb_big_exercise.text.toString()
//                        R.id.rb_big_edu->big_category=rb_big_edu.text.toString()
//                        R.id.rb_big_other->big_category=rb_big_other.text.toString()
//                    }

                }
            }
        }

    }


}