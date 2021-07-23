package aslan.aslanov.videocapture.network.service

import aslan.aslanov.videocapture.model.registerModel.UserRequestBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.child.ChildResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String,
    ): Response<ChildResponse>

    @Headers("Content-Type: application/json")
    @POST("user")
    suspend fun user(
        @Header("Authorization") authorization: String,
        @Body userValidation: UserRequestBody,
    ): Response<ChildResponse>



}