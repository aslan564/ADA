package aslan.aslanov.videocapture.viewModel.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.model.user.child.Reportable
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.network.Status
import aslan.aslanov.videocapture.repository.VideoRepository
import aslan.aslanov.videocapture.util.getVideoFile
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.recordVideo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class VideoViewModel : ViewModel() {
    private val repository = VideoRepository()
    private var _videoList = MutableLiveData<List<VideoPojo>>()
    val videoList: LiveData<List<VideoPojo>>
        get() = _videoList

    private var _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    private fun addVideoToDatabase(
        videoRequestBody: MultipartBody.Part,
        onComplete: (Status) -> Unit
    ) = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.addVideo(it, videoRequestBody) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        getVideos() {
                            _errorMessage.value = "list refreshed"
                        }
                        onComplete(Status.SUCCESS)
                    }
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onComplete(Status.LOADING)
                    }
                    Status.ERROR -> {
                       _errorMessage.value = response.message.toString()
                        logApp(response.message.toString())
                        onComplete(Status.LOADING)
                    }
                }
            }
        }
    }

    fun getVideos(onComplete: (String) -> Unit) = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.getAllVideo(it) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        response.data?.let { newList ->
                            _videoList.value = newList.video
                            onComplete("list refreshed")
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

    fun videoCanCreate(onComplete: (Reportable?, String?) -> Unit) = viewModelScope.launch {
        SharedPreferenceManager.token?.let {
            repository.videoCanCreate(it) { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        logApp(response.data.toString())
                        response.data?.let { video ->
                            onComplete(video, "success")
                        }
                    }
                    Status.LOADING -> {
                        logApp(response.status.name)
                        onComplete(null, "loading")
                    }
                    Status.ERROR -> {
                        _errorMessage.value = response.message.toString()
                        onComplete(null, "an error occurred")
                    }
                }
            }
        }
    }

    fun uploadVideoFromGallery(onComplete: (Boolean?,String) -> Unit) = viewModelScope.launch {
        SharedPreferenceManager.videoFile?.let { videoPath ->
            getVideoFile(videoPath) { capturedVideo ->
                logApp("capturedVideo $capturedVideo")
                recordVideo(capturedVideo) { part ->
                    addVideoToDatabase(part) {response->
                        when (response) {
                            Status.SUCCESS -> {
                                    onComplete(true,capturedVideo.fileName)
                                getVideos{
                                    _errorMessage.value=it
                                }
                            }
                            Status.LOADING -> {
                                onComplete(null,capturedVideo.fileName)
                            }
                            Status.ERROR -> {
                                _errorMessage.value="video not uploaded"
                                onComplete(false,capturedVideo.fileName)
                            }
                        }
                    }
                }
            }
        }
    }
}