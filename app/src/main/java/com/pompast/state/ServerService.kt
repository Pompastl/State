package com.pompast.state

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.pompast.state.server.WebServer
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread


class ServerService : Service() {
    private val webServer = WebServer(this)
    private val lock = Object()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        thread {
            try {
                webServer.start()
                Log.d("WebServer", "Server started")

            } catch (e: IOException) {
                Log.e("WebServer", "Failed to start the server", e)
            }

            synchronized(lock) {
                lock.wait()
            }
        }

        val notification = createNotification()
        startForeground(1010, notification)

        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        webServer.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("Server Service")
            .setContentText(getString(R.string.foreground_notification))
            .setSmallIcon(R.drawable.cloud)


        val stopIntent = Intent(this, NotifyActivity::class.java).apply {
            action = "com.pompast.state.STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        notificationBuilder.addAction(
            R.drawable.close,
            "Stop Service",
            stopPendingIntent
        )

        return notificationBuilder.build()
    }

}