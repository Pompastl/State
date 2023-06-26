package com.pompast.state.state.phoneState

import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import com.pompast.state.R


class Beeline(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun type() : String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        return when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> "${context.getString(R.string.device_line)} Unknown"
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> "Сеть: 2G"
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> "Сеть: 3G"
            TelephonyManager.NETWORK_TYPE_LTE -> "Сеть: 4G"
            TelephonyManager.NETWORK_TYPE_NR -> "Сеть: 5G"
            else -> "Unknown"
        }
    }
}