package aslan.aslanov.videocapture.ui.fragment.video

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.databinding.FragmentVideosBinding
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager.videoFile
import aslan.aslanov.videocapture.model.user.child.Reportable
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.service.DownloadService
import aslan.aslanov.videocapture.ui.fragment.video.adapter.VideoAdapter
import aslan.aslanov.videocapture.util.*
import aslan.aslanov.videocapture.util.AppConstants.CAMERA_RECORD_TIME_LIMIT
import aslan.aslanov.videocapture.viewModel.video.VideoViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.io.File
import java.util.*


class VideosFragment : Fragment() {
    private val binding by lazy { FragmentVideosBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<VideoViewModel>()
    private lateinit var requestPermission: ActivityResultLauncher<Array<String>>
    private lateinit var requestActivityForResult: ActivityResultLauncher<Intent>
    private val adapterVideo =
        VideoAdapter(onComplete = ::onCompleteLayout, isSize = this@VideosFragment::isSize)

    private fun isSize(size: Int): Unit = with(binding) {
        if (size <= 1) {
            recyclerViewVideo.visibility = View.GONE
        } else {
            recyclerViewVideo.visibility = View.VISIBLE
        }
    }

    private fun onCompleteLayout(videoPojo: VideoPojo?, status: Boolean) {
        if (videoPojo == null && status) {
            isCameraReady()
        } else {
            makeToast(videoPojo.toString(), requireContext())

        }
    }


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

        mainContainer.setOnRefreshListener {
            viewModel.getVideos {
                makeToast(it, requireContext())
            }
            mainContainer.isRefreshing = false
        }

        buttonTakeVideo.setOnClickListener {
            isCameraReady()
        }
    }


    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData(): Unit = with(viewModel) {
        getVideos {
            logApp(it)
        }
        errorMessage.observe(viewLifecycleOwner, { message ->
            message?.let {
                makeSnackBar(it, requireView())
            }
        })

        videoList.observe(viewLifecycleOwner, { video ->
            if (video != null) {
                // logApp("----------$video")
                if (video.isEmpty()) {
                    binding.groupTakeVideo.visibility = View.VISIBLE
                } else {
                    binding.groupTakeVideo.visibility = View.GONE
                }
                setListToAdapter(video)
            } else {
                binding.groupTakeVideo.visibility = View.VISIBLE
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListToAdapter(video: List<VideoPojo>) {
        val addFirstVideo = VideoPojo(null, null, null, null, null, null, null)
        lifecycleScope.launch {
            adapterVideo.refreshList(video)
            adapterVideo.addFirstRow(addFirstVideo)
            adapterVideo.notifyDataSetChanged()
            binding.recyclerViewVideo.adapter = adapterVideo
        }
    }



    private fun isCameraReady() {
        viewModel.videoCanCreate { videoCanCreate: Reportable?, _: String? ->
            videoCanCreate?.let {
                if (videoCanCreate.status) {
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
                                            .setAction("Give Permission") {
                                                val cameraPermission =
                                                    arrayOf(Manifest.permission.CAMERA)
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
                                    captureVideo { file ->
                                        videoFile = file.toURI().path
                                        Log.d(TAG, "isCameraReady: $file")
                                    }
                                }
                            }
                        }
                        val closeDialogButton =
                            view.findViewById<ImageButton>(R.id.image_button_close)
                        closeDialogButton.setOnClickListener {
                            if (alertDialog.isShowing) {
                                alertDialog.dismiss()
                            } else {
                                logApp(" +++++++++++ alert not shoving ")
                            }
                        }
                    }
                } else {
                    makeSnackBar(videoCanCreate.description, requireView())
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
                    captureVideo { file ->
                        videoFile = file.toURI().path
                        Log.d(TAG, "registerLauncher: $file")
                    }
                }
            }
        requestActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intent = result.data
                    intent?.let { intentData ->
                        logApp("requestActivityForResult :${intentData.data.toString()}")
                        try {
                            intentData.data?.let {
                                val intentService = Intent(requireContext(), DownloadService::class.java)
                                requireActivity().startService(intentService)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            logApp(e.stackTraceToString())
                        }
                    } ?: logApp("result data null qayidir ")
                } else {
                    logApp("resultCode not okay ")
                }
            }
    }

    private fun captureVideo(onCompletionListener: (File) -> Unit) {
        try {
            getVideoFile(requireContext()) { videoFile ->
                Log.d(TAG, "video file is ::: $videoFile")
                val fileProvider = FileProvider.getUriForFile(
                    requireContext(),
                    "aslan.aslanov.videocapture.provider",
                    videoFile
                )
                logApp("fileProvider -- -$fileProvider")
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_DURATION_LIMIT, CAMERA_RECORD_TIME_LIMIT)
                    putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
                    putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)

                    flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                }
                requestActivityForResult.launch(intent)
                onCompletionListener(videoFile)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    companion object {
        private const val TAG = "VideosFragment"
    }
}


