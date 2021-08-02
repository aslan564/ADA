package aslan.aslanov.videocapture.model.networkError


import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "error") val error: String?,
    @Json(name = "message") val message: String?,
    @Json(name = "status") val status: Int?,
){
}