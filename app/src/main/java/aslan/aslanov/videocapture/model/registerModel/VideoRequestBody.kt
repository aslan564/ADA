package aslan.aslanov.videocapture.model.registerModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import okhttp3.RequestBody

@JsonClass(generateAdapter = true)
data class VideoRequestBody(
    @Json(name = "file") val fileName: String,
    @Json(name = "type") val videoFile: RequestBody,
) {

}