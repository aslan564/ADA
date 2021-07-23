package aslan.aslanov.videocapture.network.service

import aslan.aslanov.videocapture.model.registerModel.PhoneRequestBody
import aslan.aslanov.videocapture.model.registerModel.CodeValidationBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.UserResponse
import aslan.aslanov.videocapture.model.validation.ValidationResponse
import retrofit2.Response
import retrofit2.http.*

interface AccountService {
    @Headers("Content-Type: application/json")
    @POST("auth")
    suspend fun login(
        @Body phoneNumber: PhoneRequestBody
    ):Response<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("auth/{userId}/validate")
    suspend fun validate(
        @Path("userId")userId:String,
        @Body codeValidationCode: CodeValidationBody
    ):Response<ValidationResponse>

    @GET("user/check")
    suspend fun userCheck(
        @Header("Authorization") authorization: String,
    ):Response<UserCheck>

}