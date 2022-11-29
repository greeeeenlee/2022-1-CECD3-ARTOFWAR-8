package com.example.artofwar.activity

import java.io.Serializable

class UserVideo (
    var name:String?,
    var upload_time: String?,
    var mjclass: String?,
    var subclass: String?,
    var status: String?,
    var status_detail:String?,
    var image_ext:String?,
    var introduction: String?,
    var storage_key: String?,
    var storage_url:String?
):Serializable{}