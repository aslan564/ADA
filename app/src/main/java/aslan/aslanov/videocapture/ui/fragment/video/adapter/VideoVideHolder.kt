package aslan.aslanov.videocapture.ui.fragment.video.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.videocapture.databinding.LayoutVideoItemBinding
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.model.video.VideoResponse

class VideoVideHolder(private val binding: LayoutVideoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(videoItem: VideoPojo): Unit = with(binding) {
        executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): VideoVideHolder {
            val view =
                LayoutVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return VideoVideHolder(view)
        }
    }

}
