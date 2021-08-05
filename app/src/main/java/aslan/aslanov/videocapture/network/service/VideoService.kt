package aslan.aslanov.videocapture.network.service

import aslan.aslanov.videocapture.model.user.child.Reportable
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.model.video.VideoResponse
import okhttp3.MultipartBody
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
    ):Response<Reportable>
    @Multipart
    @POST("video")
    suspend fun videoVideoIdPost(
        @Header("Authorization") auth: String,
        @Path("videoId") videoId: Int,
        @Part file: MultipartBody.Part,
    ): Response<VideoPojo>
}