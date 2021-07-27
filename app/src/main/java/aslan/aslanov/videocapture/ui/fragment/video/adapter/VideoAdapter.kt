package aslan.aslanov.videocapture.ui.fragment.video.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import aslan.aslanov.videocapture.databinding.LayoutItemTakeVideoBinding
import aslan.aslanov.videocapture.databinding.LayoutVideoItemBinding
import aslan.aslanov.videocapture.model.video.VideoDiffUtil
import aslan.aslanov.videocapture.model.video.VideoPojo
import aslan.aslanov.videocapture.util.logApp

class VideoAdapter(private val onComplete: (List<VideoPojo>) -> Unit) :
    ListAdapter<VideoPojo, VideoVideHolder>(VideoDiffUtil()) {
    //private val oldList: ArrayList<VideoPojo> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVideHolder {
        val videoView =
            LayoutVideoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return VideoVideHolder.from(videoView)
    }

    override fun onBindViewHolder(holder: VideoVideHolder, position: Int) {
        holder.bind(getItem(position))
        onComplete(currentList)
    }

    override fun getItemViewType(position: Int): Int {

        return if (itemCount - (position + 1) == 0) {
            1
        } else {
            2
        }
    }




    /* fun refreshList(newList: List<VideoPojo>) {
         val diffCallback = VideoDiffUtil(oldList, newList)
         val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
         oldList.clear()
         oldList.addAll(newList)
         diffUtilResult.dispatchUpdatesTo(this)
         logApp("////////////// $newList")
     }*/
}