package aslan.aslanov.videocapture.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.ui.activity.VideoActivity
import aslan.aslanov.videocapture.util.NotificationConstant.MY_NOTIFICATION_SERVICE
import aslan.aslanov.videocapture.util.NotificationConstant.NOTIFICATION_CHANNEL_ID
import aslan.aslanov.videocapture.util.NotificationConstant.UPLOAD_NOTIFICATION_ID
import aslan.aslanov.videocapture.util.logApp
import aslan.aslanov.videocapture.viewModel.video.VideoViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DownloadService : Service() {
    private val viewModel = VideoViewModel()
    override fun onBind(p0: Intent?): IBinder? {
        logApp("1 onBind: bura girdi")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onStartCommand: 2 onCreate: bura girdi")
        uploadVideo()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: 3  onStartCommand: bura girdi")
        return START_STICKY
    }


    private fun uploadVideo() {
        viewModel.uploadVideoFromGallery {status,message->
            when (status) {
                true -> {
                    Log.d(TAG, "uploadVideo: $message")
                    viewModel.getVideos {
                        SharedPreferenceManager.videoFile=null
                        startForeground("video uploading completed!!!",message)
                        this@DownloadService.stopSelf()
                    }
                }
                false -> {
                    Log.d(TAG, "uploadVideo: $message")
                    this@DownloadService.stopSelf()
                }
                null -> {
                    Log.d(TAG, "uploadVideo: $message")
                    startForeground("video uploading...",message)
                }
            }
        }
    }
    private fun startForeground(description: String, title: String){
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                MY_NOTIFICATION_SERVICE
            }
        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(description)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(UPLOAD_NOTIFICATION_ID, notification)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String):String {
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }
}

private const val TAG = "DownloadService"