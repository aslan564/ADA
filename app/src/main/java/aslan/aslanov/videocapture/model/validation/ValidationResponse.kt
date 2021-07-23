package aslan.aslanov.videocapture.model.validation


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ValidationResponse(
    @Json(name = "expiration") val expiration: String,
    @Json(name = "token") val token: String,
    @Json(name = "validated") val validated: Boolean
):Parcelable