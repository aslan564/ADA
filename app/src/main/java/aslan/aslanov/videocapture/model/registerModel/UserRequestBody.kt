package aslan.aslanov.videocapture.model.registerModel


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserRequestBody(
    @Json(name = "privacyContract") val privacyContract: Boolean,
    @Json(name = "reportContract") val reportContract: Boolean,
    @Json(name = "child") val child: Child,
    @Json(name = "email") val email: String
)