package aslan.aslanov.videocapture

import android.app.Application
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager

class VideoCaptureApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceManager.instance(applicationContext)
    }
}