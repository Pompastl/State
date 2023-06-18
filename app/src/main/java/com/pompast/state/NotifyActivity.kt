package com.pompast.state

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class NotifyActivity : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "com.pompast.state.STOP_SERVICE") {
            // Получение ссылки на службу и остановка службы
            val serviceIntent = Intent(context, ServerService::class.java)
            context.stopService(serviceIntent)
        }
    }


}