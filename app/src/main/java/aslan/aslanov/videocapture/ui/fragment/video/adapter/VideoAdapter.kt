package aslan.aslanov.videocapture.ui.fragment.video.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import aslan.aslanov.videocapture.model.video.VideoDiffUtil
import aslan.aslanov.videocapture.model.video.VideoPojo

private const val TAG = "VideoAdapter"

class VideoAdapter(private val onComplete: (VideoPojo?, Boolean) -> Unit) :
    RecyclerView.Adapter<VideoVideHolder>() {
    private val oldList = ArrayList<VideoPojo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVideHolder {
        return VideoVideHolder.from(parent, viewType)

    }


    override fun getItemViewType(position: Int): Int {

        return if (oldList[position].id == null) {
            1
        } else {
            2
        }
    }

    override fun onBindViewHolder(holder: VideoVideHolder, position: Int) {
        holder.bind(oldList[position], onComplete)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    fun addFirstRow(newItem: VideoPojo) {
        oldList.add(newItem)
    }


    fun refreshList(newList: List<VideoPojo>) {
        val diffCallback = VideoDiffUtil(oldList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffUtilResult.dispatchUpdatesTo(this)
    }
}