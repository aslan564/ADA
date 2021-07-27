package aslan.aslanov.videocapture.ui.fragment.video

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.database.getBlobOrNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.FragmentVideosBinding
import aslan.aslanov.videocapture.model.registerModel.VideoRequestBody
import aslan.aslanov.videocapture.model.video.VideoCanCreate
import aslan.aslanov.videocapture.ui.fragment.video.adapter.VideoAdapter
import aslan.aslanov.videocapture.util.createAlertDialogAny
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.makeSnackBar
import aslan.aslanov.videocapture.util.makeToast
import aslan.aslanov.videocapture.viewModel.video.VideoViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URI
import java.util.*


class VideosFragment : Fragment() {
    private val binding by lazy { FragmentVideosBinding.inflate(layoutInflater) }
    private val adapterVideo by lazy {
        VideoAdapter() { videoPojo ->
            if (videoPojo.isNotEmpty()) {
                // binding.group.visibility = View.GONE
            } else {
                // binding.group.visibility = View.VISIBLE
            }
        }
    }
    private val viewModel by viewModels<VideoViewModel>()
    private lateinit var requestPermission: ActivityResultLauncher<Array<String>>
    private lateinit var requestActivityForResult: ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        registerLauncher()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindUI()
    }

    private fun bindUI(): Unit = with(binding) {
        lifecycleOwner = this@VideosFragment
        binding.recyclerViewVideo.adapter = adapterVideo


        buttonTakeVideo.setOnClickListener {
            isCameraReady()
        }
    }


    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData(): Unit = with(viewModel) {
        errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                makeSnackBar(it, requireView())
            }
        })

        videoList.observe(viewLifecycleOwner, { video ->
            if (video != null) {
                //makeToast(it.size.toString(), requireContext())
                binding.group.visibility = View.GONE
                adapterVideo.submitList(video)
            } else {
                binding.group.visibility = View.VISIBLE
            }
        })
    }

    private fun recordVideo(data: VideoRequestBody) {
        logApp("2 $data")
        // val reqFile = data.videoFile.toRequestBody("video/mp4".toMediaTypeOrNull());
        val multipartBody = MultipartBody.Part.createFormData("file", data.fileName, data.videoFile)
        viewModel.addVideoToDatabase(multipartBody) {
            makeToast(it, requireContext())
        }
    }

    private fun isCameraReady() {
        viewModel.videoCanCreate() { videoCanCreate: VideoCanCreate?, s: String? ->
            videoCanCreate?.let {
                if (videoCanCreate.childStatus) {
                    createAlertDialogAny(
                        requireContext(),
                        R.layout.take_video_dialog
                    ) { view, alertDialog ->
                        view.findViewById<Button>(R.id.button_iam_ready).setOnClickListener {
                            val blackSheet = view.findViewById<CheckBox>(R.id.checkbox_black_sheet)
                            val clothes = view.findViewById<CheckBox>(R.id.checkbox_clothes)
                            val fixCamera = view.findViewById<CheckBox>(R.id.checkbox_fix_camera)
                            if (!blackSheet.isChecked) {
                                setError(blackSheet)
                                return@setOnClickListener
                            } else if (!clothes.isChecked) {
                                setError(clothes)
                                return@setOnClickListener
                            } else if (!fixCamera.isChecked) {
                                setError(fixCamera)
                                return@setOnClickListener
                            } else {
                                if (ContextCompat.checkSelfPermission(
                                        requireContext(),
                                        Manifest.permission.CAMERA
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                                            requireActivity(),
                                            Manifest.permission.CAMERA
                                        )
                                    ) {
                                        Snackbar.make(
                                            it,
                                            "Permission needed for camera ",
                                            Snackbar.LENGTH_INDEFINITE
                                        )
                                            .setAction("Give Permission") { //makeToast("per", requireContext())
                                                val cameraPermission = arrayOf(Manifest.permission.CAMERA)
                                                requestPermission.launch(cameraPermission)
                                                alertDialog.dismiss()
                                            }.show()
                                    } else {
                                        val cameraPermission = arrayOf(Manifest.permission.CAMERA)
                                        requestPermission.launch(cameraPermission)
                                        alertDialog.dismiss()
                                    }
                                } else {
                                    alertDialog.dismiss()
                                    captureVideo()
                                }
                            }
                        }
                    }
                }else{
                    makeSnackBar(videoCanCreate.descriptionInfo,requireView())
                }
            }
        }
    }

    private fun setError(checkBox: CheckBox) {
        checkBox.error = getString(R.string.required)
    }

    private fun registerLauncher() {
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                if (permission.containsValue(false)) {
                    makeToast("Permission denied!!", requireContext())
                } else {
                    captureVideo()
                }
            }
        requestActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                activityResult?.let {
                    if (it.resultCode == AppCompatActivity.RESULT_OK && it.data != null) {
                        // logApp(it.data!!.data.toString())
                        try {
                            it.data?.let { videoData ->
                                val capturedVideo =
                                    requireContext().contentResolver.getFileName(videoData.data!!)
                                capturedVideo?.let {
                                    logApp("************** :${capturedVideo.fileName}  :${capturedVideo.videoFile}")
                                    recordVideo(capturedVideo)
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                            logApp(e.stackTraceToString())
                        }
                    }
                }
            }
        val videoPickGallery =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                uri?.let { uriItem ->
                    Log.d(TAG, " 9: $uriItem")
                    try {
                        val file = File(URI.create(uriItem.path))
                        logApp(file.toString())
                        // sendVideoDatabase(uriItem.toString())
                    } catch (e: Exception) {
                        println(e.message)
                        Log.d(TAG, "error: ${e.message}")
                    }
                }
            }
    }

    private fun captureVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15)
        requestActivityForResult.launch(intent)
    }


    private fun ContentResolver.getFileName(fileUri: Uri): VideoRequestBody? {
        var videoFile: VideoRequestBody? = null
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            //val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = returnCursor.getString(nameIndex)
            val fileVideo = returnCursor.getString(nameIndex).toRequestBody()
            returnCursor.getBlobOrNull(nameIndex)?.let {
                videoFile = VideoRequestBody(name, fileVideo)
            }
            returnCursor.close()
        }
        return videoFile
    }


    companion object {
        private const val TAG = "VideosFragment"
    }


    data class Video(
        val uri: Uri,
        val name: String,
        val duration: Int,
        val size: Int
    )

    val videoList = mutableListOf<Video>()


}


