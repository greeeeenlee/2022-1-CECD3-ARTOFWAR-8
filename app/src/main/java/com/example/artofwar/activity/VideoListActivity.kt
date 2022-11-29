package com.example.artofwar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.artofwar.R
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.activity_video_list.*
import retrofit2.Call
import retrofit2.Response
import com.bumptech.glide.Glide

class VideoListActivity : AppCompatActivity() {

    var UserVideoList = arrayListOf<UserVideo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)

        val access_token=intent.getStringExtra("access_token")
        var videoarr=ArrayList<JsonElement>()

        //카테고리 버튼을 클릭하는 경우
        imgbtn_category.setOnClickListener {
            val intent=Intent(this, CategoryActivity::class.java)
            intent.putExtra("access_token",access_token)
            startActivity(intent)
        }


        val service=VideoRequest.RetrofitAPI.emgMedService
        service.getVideoInfo(access_token)
            .enqueue(object :retrofit2.Callback<vlist>{
                override fun onResponse(call: Call<vlist>, response: Response<vlist>){

                    val videocount=response.body()?.videoInfo?.size()?.toInt() // 올린 동영상 개수

                    if(videocount==null || videocount==0){//비디오를 올리지 않은 경우
                        listView_video.visibility=View.GONE
                        tv_novideo.text="아직 동영상을 업로드하지 않았습니다.\n동영상을 올린 후에 확인 가능합니다."
                    }
                    else{
                        tv_novideo.visibility=View.GONE
                        for (i in 0..(videocount-1)!!){
                            val temp=response.body()!!.videoInfo[i.toString()]
                            videoarr.add(temp)

                            val temp_obj=temp.asJsonObject //jsonelement->jsonobject
                            val name=replacestr(temp_obj.get("name").toString())
                            val upload_time=replacestr(temp_obj.get("upload_time").toString())
                            val mjclass=replacestr(temp_obj.get("mjclass").toString())
                            val subclass=replacestr(temp_obj.get("subclass").toString())
                            val status=replacestr(temp_obj.get("status").toString())
                            val status_detail=replacestr(temp_obj.get("status_detail").toString())
                            val image_ext=replacestr(temp_obj.get("image_ext").toString())
                            val introduction=replacestr(temp_obj.get("introduction").toString())
                            val storage_key=replacestr(temp_obj.get("storage_key").toString())
                            val storage_url=temp_obj.get("storage_url").toString().replace("\"","")

                            val temp_videodata=UserVideo(name,upload_time,mjclass, subclass, status, status_detail, image_ext, introduction, storage_key,storage_url)
                            UserVideoList.add(i,temp_videodata)

                        }
                    }

                    val Adapter = UserVideoAdapter(this@VideoListActivity,UserVideoList)
                    listView_video.adapter=Adapter

                    //리스트뷰 클릭 이벤트
                    listView_video.setOnItemClickListener { adapterView, view, i, l ->
                        val clickedVideo=UserVideoList[i]
                        val intent= Intent(this@VideoListActivity,VideoDetailActivity::class.java)
                        intent.putExtra("VideoInfo",clickedVideo)
                        intent.putExtra("access_token",access_token)
                        startActivity(intent)
                    }

                }
                override fun onFailure(call: Call<vlist>, t:Throwable){
                    // 실패
                    val dialog=Alert_Dialog(this@VideoListActivity)
                    dialog.showDialog(11)
                    finish()
                    Log.d("LOG_test_fail",t.message.toString())
                }
            })

    }

    private fun replacestr(str:String):String{
        var finalstr=str.replace("\"","") //따옴표 제거
        finalstr=finalstr.replace("\\","") //역슬래시 제거
        return finalstr
    }
}
