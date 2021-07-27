package aslan.aslanov.videocapture.network.service

import aslan.aslanov.videocapture.model.registerModel.VideoRequestBody
import aslan.aslanov.videocapture.model.video.VideoCanCreate
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.model.video.VideoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface VideoService {

    @Multipart
    @POST("video")
    suspend fun setVideo(
        @Header("Authorization") authorization: String,
        @Part  file: MultipartBody.Part,
    ): Response<VideoPojo>

    @GET("video/list")
    suspend fun videoList(
        @Header("Authorization") authorization: String,
    ): Response<VideoResponse>

    @GET("video/can-create")
    suspend fun videoCanCreate(
        @Header("Authorization")authorization: String
    ):Response<VideoCanCreate>
}