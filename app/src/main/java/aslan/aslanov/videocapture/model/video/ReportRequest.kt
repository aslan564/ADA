package aslan.aslanov.videocapture.model.video

import com.squareup.moshi.Json

data class ReportRequest(
    @Json(name = "status") var childStatus:Boolean,
)
