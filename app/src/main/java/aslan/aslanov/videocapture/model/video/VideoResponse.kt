package aslan.aslanov.videocapture.model.video


import com.squareup.moshi.Json

data class VideoResponse(
    @Json(name = "videos")
    val video: List<VideoPojo>
)