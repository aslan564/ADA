package aslan.aslanov.videocapture.model.video


import androidx.recyclerview.widget.DiffUtil
import com.squareup.moshi.Json

data class VideoPojo(
    @Json(name = "created") val created: String?,
    @Json(name = "id") val id: Int?,
    @Json(name = "status") val status: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "updated") val updated: String?,
    @Json(name = "url") val url: String?,
    @Json(name = "weeks") val weeks: Int?
) : Comparable<VideoPojo> {
    constructor() : this(null, null, null, null, null, null, null)

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

class VideoDiffUtil() :
    DiffUtil.ItemCallback<VideoPojo>() {
    override fun areItemsTheSame(oldItem: VideoPojo, newItem: VideoPojo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VideoPojo, newItem: VideoPojo): Boolean {
        val result = oldItem.compareTo(newItem)
        return result == 0
    }


}