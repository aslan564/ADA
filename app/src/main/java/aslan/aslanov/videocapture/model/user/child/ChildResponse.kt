package aslan.aslanov.videocapture.model.user.child

import aslan.aslanov.videocapture.model.registerModel.Child
import com.squareup.moshi.Json

data class ChildResponse(
    @Json(name = "child") val child: Child,
    @Json(name = "email") val email: String,
    @Json(name = "id") val id: Int,
    @Json(name = "isReportable") val report: Reportable,
    @Json(name = "privacyContract") val privacyContract: Boolean,
    @Json(name = "reportContract") val reportContract: Boolean
)

enum class GENDER {
    MALE, FEMALE
}
