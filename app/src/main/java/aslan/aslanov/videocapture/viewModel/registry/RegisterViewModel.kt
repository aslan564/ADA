package aslan.aslanov.videocapture.viewModel.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.model.registerModel.PhoneRequestBody
import aslan.aslanov.videocapture.model.registerModel.CodeValidationBody
import aslan.aslanov.videocapture.model.user.UserCheck
import aslan.aslanov.videocapture.model.user.UserResponse
import aslan.aslanov.videocapture.model.validation.ValidationResponse
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.AccountRepository
import aslan.aslanov.videocapture.util.logApp
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val repository = AccountRepository()


    private var _errorMessageRegister = MutableLiveData<String>()
    val errorMessageRegister: LiveData<String>
        get() = _errorMessageRegister

    private var _errorMessageValidation = MutableLiveData<String>()
    val errorMessageValidation: LiveData<String>
        get() = _errorMessageValidation

    fun registerUser(
        requestBody: PhoneRequestBody,
        onCompletionListener: (UserResponse?) -> Unit
    ) = viewModelScope.launch {
        repository.registerUserWithPhone(requestBody) { response ->
            when (response.status) {
                Status.LOADING -> {
                    logApp(response.status.name)
                    onCompletionListener(null)
                }
                Status.SUCCESS -> {
                    response.data?.let {
                        logApp(it.id.toString())
                        onCompletionListener(it)
                    }
                }
                Status.ERROR -> {
                    logApp(response.message.toString())
                    _errorMessageRegister.value = response.message
                    onCompletionListener(null)
                }
            }
        }
    }

    fun userValidation(
        userId: String, codeValidationCode: CodeValidationBody,
        onCompletionListener: (ValidationResponse?) -> Unit
    ) =
        viewModelScope.launch {
            repository.validateUserCode(userId, codeValidationCode) { response ->
                when (response.status) {
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onCompletionListener(null)
                    }
                    Status.SUCCESS -> {
                        response.data?.let {
                            logApp(it.validated.toString())
                            onCompletionListener(it)
                        }
                    }
                    Status.ERROR -> {
                        logApp(response.message.toString())
                        _errorMessageValidation.value = response.message
                        onCompletionListener(null)
                    }
                }
            }
        }

 /*   fun userCheck(
        token: String,
        onCompletionListener: (UserCheck?) -> Unit
    ) =
        viewModelScope.launch {
            repository.userCheck(token) { response ->
                when (response.status) {
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onCompletionListener(null)
                    }
                    Status.SUCCESS -> {
                        response.data?.let {
                            logApp(it.profileCompleted.toString())
                            onCompletionListener(it)
                        }
                    }
                    Status.ERROR -> {
                        logApp(response.message.toString())
                        _errorMessageValidation.value = response.message
                        onCompletionListener(null)
                    }
                }
            }
        }*/
}