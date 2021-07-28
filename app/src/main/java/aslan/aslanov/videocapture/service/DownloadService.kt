package aslan.aslanov.videocapture.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import aslan.aslanov.videocapture.R
import aslan.aslanov.videocapture.ui.activity.VideoActivity
import aslan.aslanov.videocapture.util.NotificationConstant.NOTIFICATION_CHANNEL_ID
import aslan.aslanov.videocapture.util.NotificationConstant.UPLOAD_NOTIFICATION_ID
import aslan.aslanov.videocapture.util.NotificationConstant.VIDEO_REQUEST_BODY
import aslan.aslanov.videocapture.util.logApp

class DownloadService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        logApp("1 onBind: bura girdi")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        logApp("2 onCreate: bura girdi")
        uploadVideo()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logApp("3  onStartCommand: bura girdi")
        showNotification()
        return START_STICKY
    }

    private fun showNotification() {

        val name=Intent().getStringExtra(VIDEO_REQUEST_BODY)
        logApp("----------------- ${name.toString()}")
        val notificationIntent = Intent(this, VideoActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val adaImage = BitmapFactory.decodeResource(resources, R.drawable.ada_login)

        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(adaImage)
            .bigLargeIcon(null)

        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ada_login)
            .setLargeIcon(adaImage)
            .setContentTitle("Upload Video")
            .setContentText("textContent")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setStyle(bigPicStyle)
            .setAutoCancel(true)
            .build()

        notification.flags!=Notification.FLAG_AUTO_CANCEL
        startForeground(UPLOAD_NOTIFICATION_ID, notification)


    }

    private fun uploadVideo() {

    }

    private fun createNotificationChannel() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Upload Video",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}