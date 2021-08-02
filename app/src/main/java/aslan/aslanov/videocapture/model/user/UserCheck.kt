package aslan.aslanov.videocapture.model.user


import com.squareup.moshi.Json

data class UserCheck(
    @Json(name = "profileCompleted") val profileCompleted: Boolean
)