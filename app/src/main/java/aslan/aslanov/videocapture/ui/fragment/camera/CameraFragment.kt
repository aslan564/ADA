package aslan.aslanov.videocapture.ui.fragment.camera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import aslan.aslanov.videocapture.databinding.FragmentCameraBinding
import aslan.aslanov.videocapture.util.logApp

class CameraFragment : Fragment(), SurfaceHolder.Callback {
    private val binding by lazy { FragmentCameraBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {

        }
    }

    companion object {
        private const val TAG = "CameraFragment"
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        logApp("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        logApp("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        logApp("Not yet implemented")
    }
}