package com.pompast.state.state.phoneState

import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.PowerManager
import com.pompast.state.R


class Battery(val context: Context) {
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
        return "$charge %"
    }

    fun isCharging(): String {
        if (batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) in pluggedTypes)
            return "${context.getString(R.string.device_battery_charge)} \uD83D\uDD0B"

        return "${context.getString(R.string.device_battery_discharge)} \uD83E\uDEAB"
    }

    fun ecoType(): String {

        if (isCharging() == "${context.getString(R.string.device_battery_charge)} \uD83D\uDD0B") {
            return ""
        }

        if (powerManager!!.isPowerSaveMode) {
            return "${context.getString(R.string.eco_on)} \uD83C\uDF31"
        }
        return "${context.getString(R.string.eco_off)} ‼️"
    }

    fun chargingCurrent(): String {
        val voltage: Float = batteryStatus!!.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) / 1000f
        return if (isCharging() == "${context.getString(R.string.device_battery_charge)} \uD83D\uDD0B") "${context.getString(R.string.voltage_power)} $voltage V" else ""
    }



    override fun toString(): String {
        return "${level()}  ${isCharging()}"
    }
}