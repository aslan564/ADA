package aslan.aslanov.videocapture.ui.fragment.video.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.videocapture.model.video.VideoDiffUtil
import aslan.aslanov.videocapture.model.video.VideoPojo

class VideoAdapter : RecyclerView.Adapter<VideoVideHolder>() {
    private val oldList = arrayListOf<VideoPojo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVideHolder {
        return VideoVideHolder.from(parent)
    }

    override fun onBindViewHolder(holder: VideoVideHolder, position: Int) {
        holder.bind(oldList[position])
    }

    override fun getItemCount() = oldList.size

    fun refreshList(newList: List<VideoPojo>) {
        val diffCallback = VideoDiffUtil(oldList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}