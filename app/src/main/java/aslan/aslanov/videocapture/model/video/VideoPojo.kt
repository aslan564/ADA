package aslan.aslanov.videocapture.model.video


import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class VideoPojo(
    @Json(name = "created") val created: String,
    @Json(name = "id") val id: Int,
    @Json(name = "status") val status: String,
    @Json(name = "title") val title: String,
    @Json(name = "updated") val updated: String,
    @Json(name = "url") val url: String,
    @Json(name = "weeks") val weeks: Int
) : Comparable<VideoPojo> {
    override fun compareTo(other: VideoPojo): Int {
        return if (
            this.id == other.id &&
            this.status == other.status &&
            this.title == other.title &&
            this.url == other.url &&
            this.weeks == other.weeks &&
            this.updated == other.updated &&
            this.created == other.created
        ) {
            0
        } else {
            1
        }
    }

}

class VideoDiffUtil(private val oldList: List<VideoPojo>, private val newList: List<VideoPojo>) :
    DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[newItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val result = newList[newItemPosition].compareTo(oldList[oldItemPosition])
        return result == 0
    }

}