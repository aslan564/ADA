package aslan.aslanov.videocapture.viewModel.registry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.model.registerModel.UserRequestBody
import aslan.aslanov.videocapture.model.user.child.ChildResponse
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.UserRepository
import aslan.aslanov.videocapture.util.logApp
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    private var _childUserCanCreate = MutableLiveData<ChildResponse>()
    val childUserCreate: LiveData<ChildResponse>
        get() = _childUserCanCreate

    private var _errorMessageConfirmChildInfo = MutableLiveData<String>()
    val errorMessageConfirmChildInfo: LiveData<String>
        get() = _errorMessageConfirmChildInfo

    fun createUser(token: String, userRequestBody: UserRequestBody) = viewModelScope.launch {
        logApp("UserViewModel : $userRequestBody")

         repository.createChildUser(token, userRequestBody){response->
             when (response.status) {
                 Status.ERROR -> {
                     logApp("UserViewModel : ${response.message}")
                     _errorMessageConfirmChildInfo.value=response.message.toString()
                 }
                 Status.LOADING -> {
                     logApp("UserViewModel : ${Status.LOADING}")
                 }
                 Status.SUCCESS -> {
                     _childUserCanCreate.value = response.data
                     logApp("UserViewModel : ${response.data}")
                 }
             }
        }
    }
    fun getChildUserDate()=viewModelScope.launch {
        SharedPreferenceManager.token?.let {token->
            repository.getUserData(token){response->
                when (response.status) {
                    Status.ERROR -> {
                        logApp("UserViewModel : ${response.message}")
                        _errorMessageConfirmChildInfo.value=response.message.toString()
                    }
                    Status.LOADING -> {
                        logApp("UserViewModel : ${Status.LOADING}")
                    }
                    Status.SUCCESS -> {
                        _childUserCanCreate.value = response.data
                        logApp("UserViewModel : ${response.data}")
                    }
                }
            }
        }
    }
}