package aslan.aslanov.videocapture.repository

import aslan.aslanov.videocapture.model.registerModel.UserRequestBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.child.ChildResponse
import aslan.aslanov.videocapture.network.NetworkResult
import aslan.aslanov.videocapture.network.RetrofitClient
import aslan.aslanov.videocapture.util.addBearer
import aslan.aslanov.videocapture.util.catchServerError
import aslan.aslanov.videocapture.util.logApp
import java.lang.Exception

class UserRepository {
    private val serviceAccount = RetrofitClient.userService

    suspend fun createChildUser(
        token: String,
        userRequestBody: UserRequestBody,
        onComplete: (NetworkResult<ChildResponse>) -> Unit
    ) {
        try {
            val response = serviceAccount.user(addBearer(token), userRequestBody)
            logApp("createChildUser : $response")
            if (response.isSuccessful && response.code() == 200) {
                val validateData = response.body()
                validateData?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error("validate data null"))
            } else {
                catchServerError<ChildResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }

    suspend fun getUserData(
        token: String,
        onComplete: (NetworkResult<ChildResponse>) -> Unit
    ) {
        try {
            val response = serviceAccount.getUserInfo(addBearer(token))
            if (response.isSuccessful && response.code() == 200) {
                val validateData = response.body()
                validateData?.let {
                    onComplete(NetworkResult.success(it))
                }
            } else {
                catchServerError<ChildResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }

}