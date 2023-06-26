package com.pompast.state.server

import android.annotation.SuppressLint
import android.content.Context
import com.pompast.state.R
import fi.iki.elonen.NanoHTTPD
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

class WebServer(private val  context: Context) : NanoHTTPD(8080) {



    lateinit var batteryPercent: String
    lateinit var batteryStatus: String
    lateinit var beelineType: String
    lateinit var cpuTemp: String
    lateinit var eco: String
    lateinit var chargingCurrent: String



    lateinit var batteryTemp: String
    lateinit var currentTime: Date


    @SuppressLint("SimpleDateFormat")
    override fun serve(session: IHTTPSession): Response {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
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


}