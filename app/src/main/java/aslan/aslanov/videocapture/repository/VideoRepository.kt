package aslan.aslanov.videocapture.repository

import aslan.aslanov.videocapture.model.registerModel.VideoRequestBody
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.model.video.VideoResponse
import aslan.aslanov.videocapture.network.NetworkResult
import aslan.aslanov.videocapture.network.RetrofitClient
import aslan.aslanov.videocapture.util.addBearer
import aslan.aslanov.videocapture.util.catchServerError
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VideoRepository {
    private val service = RetrofitClient.videoService

    suspend fun addVideo(
        token: String,
        videoRequestBody: MultipartBody.Part,
        onComplete: (NetworkResult<VideoPojo>) -> Unit
    ) {
        val response = service.setVideo(addBearer(token), videoRequestBody)
        try {
            if (response.isSuccessful && response.code() == 200) {
                val data = response.body()
                data?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error("data body not loaded "))
            } else {
                catchServerError<VideoPojo>(response.errorBody()) {
                    onComplete(it)
                }
            }

        } catch (e: Exception) {
            onComplete(NetworkResult.error(e.message ?: "catch response not convert "))
        }
    }

    suspend fun getAllVideo(
        token: String, onComplete: (NetworkResult<VideoResponse>) -> Unit
    ) {
        val response = service.videoList(" Bearer ".plus(token))
        try {
            if (response.isSuccessful && response.code() == 200) {
                val data = response.body()
                data?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error("data body not loaded "))
            } else {
                catchServerError<VideoResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }

        } catch (e: Exception) {
            onComplete(NetworkResult.error(e.message ?: "catch response not convert "))
        }
    }
}