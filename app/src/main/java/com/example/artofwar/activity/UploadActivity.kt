package com.example.artofwar.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.provider.Settings
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.RadioGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import com.example.artofwar.R
import kotlinx.android.synthetic.main.activity_upload.*
import retrofit2.Response
import java.io.File
import retrofit2.Call
import java.text.SimpleDateFormat
import kotlin.Exception


class UploadActivity : AppCompatActivity() {

    val REQUEST_READ_EXTERNAL_STORAGE=1000
    val OPEN_GALLERY=1
    val SELECT_IMAGE=10
    var imageFileName=""
    var videoFileName=""
    var imageFilePath=""
    var videoFilePath=""
    var videoFileType=""
    var imageFileType=""
    var videoFileSize=0.0

    var finalvideoFilePath=""
    var finalimageFilePath=""

    lateinit var videodatauri :Uri
    lateinit var imagedatauri :Uri

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
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }
        imgbtn_home.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("access_token",access_token)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
            intent.putExtra("access_token",access_token)
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
            else if(videoFileSize!=0.0 && videoFileSize>1000) {
                val dialog=Alert_Dialog(this)
                dialog.showDialog(19)
            }
            else {//모두 입력한 경우

                //동영상 이름 설정
                val currentTime=System.currentTimeMillis()
                val sdf= SimpleDateFormat("yyMMdd_HHmmss")
                val nowtime=sdf.format(currentTime)
                Log.d("LOG_test",nowtime.toString())
                val suffix="video_"+nowtime.toString()+"."
                val videofile_address=suffix+videoFileType //파일 이름
                lateinit var imagefile_address:String


                lateinit var image:String //썸네일 선택 여부
                if(image_address.isEmpty()){//썸네일을 선택 안한 경우
                    image="N"
                }
                else{//썸네일을 선택한 경우
                    image="Y"
                    imagefile_address="image_"+suffix+imageFileType
                }

                val awsCredentials= BasicAWSCredentials("accessKey","secretKey")
                val s3Client= AmazonS3Client(awsCredentials, Region.getRegion(Regions.US_WEST_1))

                val transferUtility= TransferUtility.builder().s3Client(s3Client).context(this).defaultBucket("aowbuket").build()
                TransferNetworkLossHandler.getInstance(this)

                //var uploadvideofile=File("/sdcard/Pictures/testvideo.mp4")
                // 동일한 경로 : /storage/emulated/0/Pictures/sdcard_testvideo.mp4

                var objectMetadata=ObjectMetadata()
                objectMetadata.addUserMetadata("ContentType","video/"+videoFileType)
                var uploadvideofile=File(finalvideoFilePath)

                var uploadObserver=try{
                    transferUtility.upload(videofile_address,uploadvideofile,objectMetadata)
                }catch(e:Exception){
                    Log.d("upload_fail",e.toString())
                }
                if(image=="Y"){//썸네일이 있다면 업로드
                    var imageobjectMetadata=ObjectMetadata()
                    imageobjectMetadata.addUserMetadata("ContentType","image/"+imageFileType)
                    var uploadimagefile=File(finalimageFilePath)
                    val uploadObserver_image=transferUtility.upload(imagefile_address,uploadimagefile,imageobjectMetadata)
                }

                val service=UploadRequest.RetrofitAPI.emgMedService
                service.uploadVideo(access_token,videofile_address,image,imageFileType,title,big_category,small_category)
                    .enqueue(object:retrofit2.Callback<UploadResponseBody>{
                        override fun onResponse(
                            call: Call<UploadResponseBody>,
                            response: Response<UploadResponseBody>
                        ) {
                            Log.d("LOG_test_upload",response.toString())
                            uploadresponse(response.code().toString(),access_token)
                        }

                        override fun onFailure(call: Call<UploadResponseBody>, t: Throwable) {
                            Log.d("LOG_test_upload_fail",t.toString())
                        }

                    })

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
        intent.setType("video/*")
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
                    videoFileSize= cursor.getLong(sizeIndex)/1000000.0 //메가바이트 단위
                    videoFileName=name
                    Log.d("LOG_test_video_name",videoFileName)
                    Log.d("LOG_test_video_size",videoFileSize.toString())

                    tv_file_address.setText(name)
                }
                val mime=MimeTypeMap.getSingleton()
                videoFileType=mime.getExtensionFromMimeType(contentResolver.getType(videodatauri)).toString()
                Log.d("LOG_test_video_type",videoFileType)

                val fileFileName=getFileStreamPath("Android")
                val getFileName=fileFileName.path
                Log.d("LOG_test_video_stream",getFileName)

                Log.d("LOG_test_uri",videodatauri.toString())
                videoFilePath=videodatauri.path.toString()
                Log.d("LOG_test_path",videoFilePath)
                val videofile=File(videoFilePath)

//                Log.d("LOG_test_video_parent",videofile.parent)
//                Log.d("LOG_test_video_parent2",videofile.parentFile.name)
                val stringfile=videofile.parentFile.name
                val stringfilearr= stringfile.split(":")
                val testparent=stringfilearr[stringfilearr.size-1]
//                Log.d("LOG_test_video_parent3",testparent)
//                Log.d("LOG_test_video_path2",videofile.canonicalPath)
//                Log.d("LOG_test_video_external",Environment.getExternalStorageDirectory().absolutePath)
//                Log.d("LOG_test_video_docu",videodatauri.authority.toString())
//                Log.d("LOG_test_video_direct",Environment.DIRECTORY_PICTURES)

                val docId=DocumentsContract.getDocumentId(videodatauri)
                val split=arrayOf(docId.split(":"))
                val type=split[0]
                if("primary".equals(type)){
                    Log.d("LOG_test_video_1",Environment.getExternalStorageDirectory().toString()+"/"+split[1])
                }

                finalvideoFilePath=Environment.getExternalStorageDirectory().absolutePath+"/"+testparent+"/"+videoFileName
                Log.d("LOG_test_finalpath",finalvideoFilePath)

            }
            else if(requestCode==SELECT_IMAGE){
                imagedatauri=data?.data!!
                data?.data?.let { it->
                    contentResolver.query(it,null,null,null,null)}?.use {
                        cursor->
                    val nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val name=cursor.getString(nameIndex)
                    imageFileName=name
                    tv_imagefile_address.setText(name)
                }
                val mime=MimeTypeMap.getSingleton()
                imageFileType=mime.getExtensionFromMimeType(contentResolver.getType(imagedatauri)).toString()

                imageFilePath=imagedatauri.path.toString()

                val imagefile=File(imageFilePath)

                val stringfile=imagefile.parentFile.name
                val stringfilearr= stringfile.split(":")
                val testparent=stringfilearr[stringfilearr.size-1]

                finalimageFilePath=Environment.getExternalStorageDirectory().absolutePath+"/"+testparent+"/"+imageFileName

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