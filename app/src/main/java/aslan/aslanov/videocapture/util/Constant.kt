package aslan.aslanov.videocapture.util

import okhttp3.MediaType.Companion.toMediaTypeOrNull

object NetworkConstant {
    const val BASE_URL = "http://ec2-52-17-33-184.eu-west-1.compute.amazonaws.com:8080/"
    val emailPattern: Regex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    val MEDIA_TYPE_TEXT = "file/string".toMediaTypeOrNull()
    val MEDIA_TYPE_VIDEO  = "video/mp4".toMediaTypeOrNull()

}
object NotificationConstant{
    const val NOTIFICATION_CHANNEL_ID="aslan.aslanov.videocapture"
    const val UPLOAD_NOTIFICATION_ID=123
    const val VIDEO_REQUEST_BODY="aslan.aslanov.videocapture.util"
}
enum class VideoStatus{
    PROCESSING,INAPPROPRIATE,APPROPRIATE
}