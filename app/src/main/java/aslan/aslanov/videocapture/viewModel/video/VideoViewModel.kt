package aslan.aslanov.videocapture.viewModel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.model.video.VideoCanCreate
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.VideoRepository
import aslan.aslanov.videocapture.util.logApp
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class VideoViewModel : ViewModel() {
    private val repository = VideoRepository()
    private var _videoList = MutableLiveData<List<VideoPojo>>()
    val videoList: LiveData<List<VideoPojo>>
        get() = _videoList

    private var _videoListSize = MutableLiveData<Int>()
    val videoListSize: LiveData<Int>
        get() = _videoListSize

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        getVideos()
    }

    fun addVideoToDatabase(
        videoRequestBody: MultipartBody.Part,
        onComplete:(String)->Unit
    ) = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.addVideo(it, videoRequestBody) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        getVideos()
                        onComplete("video yuklendi")
                    }
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onComplete("video yuklemesi basladi")
                    }
                    Status.ERROR -> {
                        _errorMessage.value = response.message.toString()
                        onComplete("video yuklemesi mumkun olmadi")
                    }
                }
            }
        }
    }

    private fun getVideos() = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.getAllVideo(it) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        response.data?.let { newList ->
                            _videoList.value = newList.video
                        }
                    }
                    Status.LOADING -> {
                        logApp(response.status.name)
                    }
                    Status.ERROR -> {
                        _errorMessage.value = response.message.toString()
                    }
                }
            }
        }
    }
    fun videoCanCreate(onComplete: (VideoCanCreate?,String?) -> Unit)=viewModelScope.launch{
        SharedPreferenceManager.token?.let {
            repository.videoCanCreate(it){response->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        response.data?.let { video ->
                            onComplete(video,"success")
                        }
                    }
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onComplete(null,"loading")
                    }
                    Status.ERROR -> {
                        _errorMessage.value = response.message.toString()
                        onComplete(null,"an error occurred")
                    }
                }
            }
        }
    }
}