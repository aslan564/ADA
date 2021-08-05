package aslan.aslanov.videocapture.ui.fragment.video.adapter

import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.videocapture.databinding.FragmentRegisterBinding
import aslan.aslanov.videocapture.databinding.LayoutTakeVideoItemBinding
import aslan.aslanov.videocapture.databinding.LayoutVideoItemBinding
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.videoIsValidate

class VideoVideHolder(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        videoItem: VideoPojo,
        onClickAction: (VideoPojo?,Boolean)->Unit
    ): Unit = with(binding) {
        if (binding is LayoutVideoItemBinding) {
            //logApp("******** $videoItem")
            val bindingVideo = binding as LayoutVideoItemBinding
            bindingVideo.pojoVideoItem = videoItem
            videoItem.videoIsValidate(
                bindingVideo.videoRecyclerViewStatus,
                bindingVideo.videoRecyclerViewStatusIcon
            )
            root.setOnClickListener {
                onClickAction(videoItem,false)
            }
            executePendingBindings()
        }else{
            val bindingVideo = binding as LayoutTakeVideoItemBinding
            bindingVideo.buttonTakeVideo.setOnClickListener {
                onClickAction(null,true)
            }
            executePendingBindings()
           // logApp("******** bu o videodan deyil menim balam ")
        }

    }

    companion object {
        fun from(parent: ViewGroup, viewType: Int): VideoVideHolder {
            return if (viewType == 1) {
                val videoView = LayoutTakeVideoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VideoVideHolder(videoView)
            } else {
                val videoView = LayoutVideoItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                VideoVideHolder(videoView)
            }
        }
    }

}

