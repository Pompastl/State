package com.pompast.state.state.phoneState

import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.PowerManager


class Battery(context: Context) {
    companion object {
        val pluggedTypes: List<Int> = listOf(
            BatteryManager.BATTERY_PLUGGED_AC,
            BatteryManager.BATTERY_PLUGGED_USB, BatteryManager.BATTERY_PLUGGED_WIRELESS)
    }

    private val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    private val batteryStatus: Intent? = context.registerReceiver(null, intentFilter)
    private var powerManager = context.getSystemService(POWER_SERVICE) as PowerManager?



    fun level(): String {
        val charge = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val mod = if
                (charge <= 30 && isCharging() != "Устройство: заряжается \uD83D\uDD0B")
            "\uD83D\uDD0C" else ""


        return "$charge % $mod"
    }

    fun isCharging(): String {
        if (batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) in pluggedTypes)
            return "Устройство: заряжается \uD83D\uDD0B"

        return "Разряд батареи \uD83E\uDEAB"
    }

    fun ecoType(): String {

        if (isCharging() == "Устройство: заряжается \uD83D\uDD0B") {
            return ""
        }

        if (powerManager!!.isPowerSaveMode) {
            return "Батарея в режиме энерго сбережения \uD83C\uDF31"
        }
        return "Батарея в режиме потребления‼️"
    }

    fun chargingCurrent(): String {
        val voltage: Float = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000f
        return if (isCharging() == "Устройство: заряжается \uD83D\uDD0B") "Сила тока: $voltage" else ""
    }



    override fun toString(): String {
        return "${level()}  ${isCharging()}"
    }
}