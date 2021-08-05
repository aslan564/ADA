package aslan.aslanov.videocapture.repository

import aslan.aslanov.videocapture.model.user.child.Reportable
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.model.video.VideoResponse
import aslan.aslanov.videocapture.network.NetworkResult
import aslan.aslanov.videocapture.network.UserService
import aslan.aslanov.videocapture.util.addBearer
import aslan.aslanov.videocapture.util.catchServerError
import aslan.aslanov.videocapture.util.logApp
import okhttp3.MultipartBody

class VideoRepository {
    private val service = UserService.videoService

    suspend fun addVideo(
        token: String,
        videoRequestBody: MultipartBody.Part,
        onComplete: (NetworkResult<VideoPojo>) -> Unit
    ) {
        try {
            onComplete(NetworkResult.loading())
            val response = service.setVideo(addBearer(token), videoRequestBody)
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
            e.printStackTrace()
            onComplete(NetworkResult.error("Please check your internet connection ${e.localizedMessage}"))
        }
    }

    suspend fun getAllVideo(
        token: String, onComplete: (NetworkResult<VideoResponse>) -> Unit
    ) {
        try {
            val response = service.videoList(addBearer(token))
            logApp(response.code().toString())
            if (response.isSuccessful && response.code() == 200) {
                val data = response.body()
                data?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error("data body not loaded "))
            } else {
                try {
                    catchServerError<VideoResponse>(response.errorBody()) {
                        onComplete(it)
                    }
                } catch (e:java.lang.Exception) {
                    e.printStackTrace()
                    logApp("no network connection ")
                }

            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }
    suspend fun videoCanCreate(token: String,onComplete: (NetworkResult<Reportable>) -> Unit){
        try {
            val response=service.videoCanCreate(addBearer(token))
            if (response.isSuccessful && response.code() == 200) {
                val data=response.body()
                data?.let {
                    onComplete(NetworkResult.success(it))
                }?:onComplete(NetworkResult.error("video not uploaded"))
            }else{
                catchServerError<Reportable>(response.errorBody()){
                    onComplete(it)
                }
            }

        } catch (e: java.lang.Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }
}