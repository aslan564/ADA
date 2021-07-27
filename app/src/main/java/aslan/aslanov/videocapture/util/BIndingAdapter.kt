package aslan.aslanov.videocapture.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.model.video.VideoPojo

@SuppressLint("SetTextI18n")
fun VideoPojo.videoIsValidate(textView: TextView, imageView: ImageView) {
    when (this.status) {
        VideoStatus.APPROPRIATE.name -> {
            textView.text = "Video Gecerli"
            imageView.setImageResource(R.drawable.ic_success)
        }
        VideoStatus.INAPPROPRIATE.name -> {
            textView.text = "Video Gecersiz"
            imageView.setImageResource(R.drawable.ic_warn)
        }
        VideoStatus.PROCESSING.name -> {
            textView.text = "Degerlendiriliyor"
            imageView.setImageResource(R.drawable.ic_time_custom)
        }
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("app:addStringWeek")
fun TextView.addWeekToText(week: String) {
    this.text = "$week .Hafta"
}

@BindingAdapter("app:groupChecking")
fun Group.addStatusChecking(size: Int?) {
    size?.let {
        if (size > 0) {
            this.visibility=View.GONE
        }else{
            this.visibility=View.VISIBLE
        }
    }
}