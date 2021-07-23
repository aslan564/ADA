package aslan.aslanov.videocapture.model.user


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @Json(name = "expiration") val expiration: String,
    @Json(name = "id") val id: Int,
    @Json(name = "location") val location: String
) : Parcelable