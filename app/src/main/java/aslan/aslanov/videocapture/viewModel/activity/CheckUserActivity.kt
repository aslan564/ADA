package aslan.aslanov.videocapture.viewModel.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.AccountRepository
import kotlinx.coroutines.launch

class CheckUserActivity : ViewModel() {
    private val repository=AccountRepository()
    fun userCheck(token:String)=viewModelScope.launch {
        repository.userCheck(token){res->
            when (res.status) {
                Status.ERROR->{}
                Status.LOADING->{}
                Status.SUCCESS->{}
            }
        }
    }
}