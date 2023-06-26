package com.pompast.state

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.pompast.state.server.WebServer
import com.pompast.state.state.phoneState.Battery
import com.pompast.state.state.phoneState.Beeline
import com.pompast.state.state.phoneState.Temper
import java.io.IOException
import java.util.*
import kotlin.concurrent.thread


class ServerService : Service() {
    private val webServer = WebServer(this)

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

            while (true) {

                upgrade()
                Thread.sleep(15_000)
            }
        }

        val notification = createNotification()
        startForeground(1010, notification)

        return START_STICKY
    }

    private fun upgrade() {
        val battery = Battery(this)
        webServer.batteryPercent = battery.level()
        webServer.batteryStatus = battery.isCharging()
        webServer.eco = battery.ecoType()
        webServer.chargingCurrent = battery.chargingCurrent()

        val temp = Temper(this)
        webServer.batteryTemp = temp.battery().toString()
        webServer.cpuTemp = temp.cpu().toString()

        val beeline = Beeline(this)
        webServer.beelineType = beeline.type()
        webServer.currentTime = Date()

    }

    override fun onDestroy() {
        super.onDestroy()
        webServer.stop()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(): Notification {
        // Создание канала уведомлений (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "CHANNEL_NAME",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Создание уведомления
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setContentTitle("Server Service")
            .setContentText("Сервис работает в фоновом режиме")
            .setSmallIcon(R.drawable.icon)
        // Добавьте другие настройки уведомления по вашему усмотрению

        val stopIntent = Intent(this, NotifyActivity::class.java).apply {
            action = "com.pompast.state.STOP_SERVICE"
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            stopIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        // Добавление действия нажатия на уведомление для остановки службы
        notificationBuilder.addAction(
            R.drawable.icon,
            "Stop Service",
            stopPendingIntent
        )

        return notificationBuilder.build()
    }

}