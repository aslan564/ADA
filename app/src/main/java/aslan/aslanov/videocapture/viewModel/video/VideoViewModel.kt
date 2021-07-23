package aslan.aslanov.videocapture.viewModel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.model.registerModel.VideoRequestBody
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.VideoRepository
import aslan.aslanov.videocapture.util.logApp
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VideoViewModel : ViewModel() {
    private val repository = VideoRepository()

    private var _videoList = MutableLiveData<List<VideoPojo>>()
    val videoList: LiveData<List<VideoPojo>>
        get() = _videoList

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        getVideos()
    }

    fun addVideoToDatabase(
        videoRequestBody: MultipartBody.Part
    ) = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.addVideo(it, videoRequestBody) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
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
}