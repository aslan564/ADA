package aslan.aslanov.videocapture.ui.fragment.video.adapter

import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.videocapture.databinding.LayoutVideoItemBinding
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.util.videoIsValidate

class VideoVideHolder(private val binding: LayoutVideoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        videoItem: VideoPojo
    ): Unit = with(binding) {
        logApp("******** $videoItem")
        pojoVideoItem = videoItem
        videoItem.videoIsValidate(videoRecyclerViewStatus, videoRecyclerViewStatusIcon)
        executePendingBindings()
    }

    companion object {
        fun from(videoView: LayoutVideoItemBinding): VideoVideHolder {
            return VideoVideHolder(videoView)
        }
    }

}

