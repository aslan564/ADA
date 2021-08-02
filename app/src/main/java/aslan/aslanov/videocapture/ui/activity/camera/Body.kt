package aslan.aslanov.videocapture.ui.activity.camera


import com.squareup.moshi.Json

data class Body(
    @Json(name = "formdata")
    val formdata: List<Formdata>,
    @Json(name = "mode")
    val mode: String
)