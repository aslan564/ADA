package aslan.aslanov.videocapture.model.user.child


import com.squareup.moshi.Json

data class Reportable(
    @Json(name = "description") val description: String,
    @Json(name = "status") val status: Boolean
)