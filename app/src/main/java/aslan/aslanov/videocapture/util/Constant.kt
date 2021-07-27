package aslan.aslanov.videocapture.util

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull

object NetworkConstant {
    const val BASE_URL = "http://ec2-52-17-33-184.eu-west-1.compute.amazonaws.com:8080/"
    val emailPattern: Regex = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    val MEDIA_TYPE_TEXT = "file/string".toMediaTypeOrNull()
    val MEDIA_TYPE_VIDEO  = "video/mp4".toMediaTypeOrNull()

}
enum class VideoStatus{
    PROCESSING,INAPPROPRIATE,APPROPRIATE
}