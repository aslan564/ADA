package aslan.aslanov.videocapture.model.registerModel

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CodeValidationBody(
    @Json(name = "validationCode") val validationCode: String
)

