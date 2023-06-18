package com.pompast.state.state.phoneState

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class Temper(private val context: Context) {

    fun battery():Float {
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val temperature = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        return temperature / 10f
    }

    fun cpu():Float {
        val cpuTempFile = File("/sys/class/thermal/thermal_zone0/temp") // Путь к файлу с температурой процессора
        if (cpuTempFile.exists()) {
            val reader = BufferedReader(FileReader(cpuTempFile))
            val temperatureString = reader.readLine()
            reader.close()
            val temperature = temperatureString?.toFloatOrNull()
            if (temperature != null) {
                return temperature / 1000f // Переводим значение в градусы Цельсия
            }
        }
        return 0f
    }

    override fun toString(): String {
        return "Температура акб: ${battery()}\n" +
                "Температура сокета: ${cpu()}"
    }

}