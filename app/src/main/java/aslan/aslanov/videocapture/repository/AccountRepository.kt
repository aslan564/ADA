package aslan.aslanov.videocapture.repository

import android.util.Log
import aslan.aslanov.videocapture.model.registerModel.PhoneRequestBody
import aslan.aslanov.videocapture.model.registerModel.CodeValidationBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.UserResponse
import aslan.aslanov.videocapture.model.validation.ValidationResponse
import aslan.aslanov.videocapture.network.NetworkResult
import aslan.aslanov.videocapture.network.UserService
import aslan.aslanov.videocapture.util.addBearer
import aslan.aslanov.videocapture.util.catchServerError
import java.lang.Exception

class AccountRepository {
    private val serviceAccount = UserService.accountServiceTwo


    suspend fun registerUserWithPhone(
        requestBody: PhoneRequestBody,
        onComplete: (NetworkResult<UserResponse>) -> Unit
    ) {
        try {
            val response = serviceAccount.login(requestBody)
            Log.d("validateUser", "getUSerData: $response")
            if (response.isSuccessful && response.code() == 201) {
                val data = response.body()
                data?.let {
                    onComplete(NetworkResult.success(it))
                } ?: onComplete(NetworkResult.error(response.message()))

            } else {
                catchServerError<UserResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }

        } catch (e: Exception) {
            onComplete(NetworkResult.error(e.message.toString()))
        }
    }

    suspend fun validateUserCode(
        userId: String,
        codeValidationCode: CodeValidationBody,
        onComplete: (NetworkResult<ValidationResponse>) -> Unit
    ) {
        try {
            val response = serviceAccount.validate(userId, codeValidationCode)
            Log.d("validateUser", "validateUser: $response")
            if (response.isSuccessful) {
                val validateData = response.body()
                validateData?.let {validateUser->

                    onComplete(NetworkResult.success(validateUser))
                } ?: onComplete(NetworkResult.error("validate data null"))
            } else {
                catchServerError<ValidationResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }

     suspend fun userCheck(
        token: String,
        onComplete: (NetworkResult<UserCheck>) -> Unit
    ) {
        try {
            val response = serviceAccount.userCheck(addBearer(token))
            if (response.isSuccessful && response.code() == 200) {
                val validateData = response.body()
                validateData?.let {
                    Log.d(TAG, "userCheck: ${it.profileCompleted}")
                    onComplete(NetworkResult.success(it))
                }
            } else {
                catchServerError<UserCheck>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error("Please check your internet connection "))
        }
    }

}

private const val TAG = "AccountRepository"