package com.pompast.state.server

import android.annotation.SuppressLint
import android.content.Context
import com.pompast.state.R
import com.pompast.state.state.phoneState.Battery
import com.pompast.state.state.phoneState.Beeline
import com.pompast.state.state.phoneState.Temper
import fi.iki.elonen.NanoHTTPD
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

class WebServer(private val  context: Context) : NanoHTTPD(8080) {



    private lateinit var batteryPercent: String
    private lateinit var batteryStatus: String
    private lateinit var beelineType: String
    private lateinit var cpuTemp: String
    private lateinit var eco: String
    private lateinit var chargingCurrent: String



    private lateinit var batteryTemp: String
    private lateinit var currentTime: Date

    @SuppressLint("SimpleDateFormat")
    override fun serve(session: IHTTPSession): Response {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        upgrade()

        return newFixedLengthResponse(
            "<body style=\"background-color: rgb(45, 43, 46);\">" +


                    "<p style=\"font-family: Arial; font-size: 100;  color: white;\" >${
                        dateFormat.format(
                            currentTime
                        )
                    }</p>" +

                    "<b style=\"color: white; font-family: Arial; font-size: 50;\">" +

                        "<p>${context.getString(R.string.html_battery)} $batteryPercent</p>" +
                        "<p>$batteryStatus</p>" +

                        "<p>$chargingCurrent</p>" +

                        "<p>${context.getString(R.string.html_temp_battery)} $batteryTemp º</p>" +
                        "<p>${context.getString(R.string.html_temp_core)} $cpuTemp º</p>" +
                        "<p>$eco</p>" +

                    "<p>$beelineType</p>" +

                    "</b>" +

                    "</body>"
        )

    }


    fun getIPAddress(): String? {
        val interfaces = NetworkInterface.getNetworkInterfaces()
        while (interfaces.hasMoreElements()) {
            val networkInterface = interfaces.nextElement()
            val addresses = networkInterface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress && address!!.hostAddress!!.indexOf(':') < 0) {
                    return address.hostAddress
                }
            }
        }
        return ""
    }

    private fun upgrade() {
        val battery = Battery(context)
        batteryPercent = battery.level()
        batteryStatus = battery.isCharging()
        eco = battery.ecoType()
        chargingCurrent = battery.chargingCurrent()

        val temp = Temper(context)
        batteryTemp = temp.battery().toString()
        cpuTemp = temp.cpu().toString()

        val beeline = Beeline(context)
        beelineType = beeline.type()
        currentTime = Date()

    }

}