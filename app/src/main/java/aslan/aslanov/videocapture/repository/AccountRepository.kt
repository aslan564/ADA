package aslan.aslanov.videocapture.repository

import android.app.usage.NetworkStats
import android.util.Log
import aslan.aslanov.videocapture.model.registerModel.PhoneRequestBody
import aslan.aslanov.videocapture.model.registerModel.CodeValidationBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.UserResponse
import aslan.aslanov.videocapture.model.validation.ValidationResponse
import aslan.aslanov.videocapture.network.NetworkResult
import aslan.aslanov.videocapture.network.RetrofitClient
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.util.addBearer
import aslan.aslanov.videocapture.util.catchServerError
import java.lang.Exception
import kotlin.math.log

class AccountRepository {
    private val serviceAccount = RetrofitClient.accountServiceTwo


    suspend fun registerUserWithPhone(
        requestBody: PhoneRequestBody,
        onComplete: (NetworkResult<UserResponse>) -> Unit
    ) {
        val response = serviceAccount.login(requestBody)
        Log.d("validateUser", "getUSerData: $response")
        try {
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
        val response = serviceAccount.validate(userId, codeValidationCode)
        Log.d("validateUser", "validateUser: $response")
        try {
            if (response.isSuccessful) {
                val validateData = response.body()
                validateData?.let {validateUser->
                    userCheck(validateUser.token) { isUser ->
                        when (isUser.status) {
                            Status.ERROR -> {
                                Log.d(TAG, "validateUserCode: ${isUser.message}")
                                Log.d(TAG, "validateUserCode: ${validateUser.token}")
                            }
                            Status.LOADING -> {
                                Log.d(TAG, "validateUserCode: ${isUser.message}")
                            }
                            Status.SUCCESS -> {
                                isUser.data?.let {
                                    if (isUser.data.profileCompleted) {
                                        Log.d(TAG, "validateUserCode: ${isUser.data.profileCompleted}")
                                    }else{
                                        Log.d(TAG, "validateUserCode: ${isUser.data.profileCompleted}")
                                    }
                                }
                            }
                        }
                    }
                    onComplete(NetworkResult.success(validateUser))
                } ?: onComplete(NetworkResult.error("validate data null"))
            } else {
                catchServerError<ValidationResponse>(response.errorBody()) {
                    onComplete(it)
                }
            }
        } catch (e: Exception) {
            onComplete(NetworkResult.error(e.message.toString()))
        }
    }

    private suspend fun userCheck(
        token: String,
        onComplete: (NetworkResult<UserCheck>) -> Unit
    ) {
        val response = serviceAccount.userCheck(addBearer(token))
        try {
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
            onComplete(NetworkResult.error(e.message.toString()))
        }
    }

}

private const val TAG = "AccountRepository"