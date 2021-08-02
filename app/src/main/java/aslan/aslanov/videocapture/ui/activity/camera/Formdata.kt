package aslan.aslanov.videocapture.ui.activity.camera


import com.squareup.moshi.Json

data class Formdata(
    @Json(name = "key")
    val key: String,
    @Json(name = "src")
    val src: String,
    @Json(name = "type")
    val type: String
)