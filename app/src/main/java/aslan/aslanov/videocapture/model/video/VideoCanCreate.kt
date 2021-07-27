package aslan.aslanov.videocapture.model.video

import com.squareup.moshi.Json

data class VideoCanCreate(
    @Json(name = "status") var childStatus:Boolean,
    @Json(name = "description") var descriptionInfo:String,
)
