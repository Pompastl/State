package com.pompast.state.server

import android.annotation.SuppressLint
import fi.iki.elonen.NanoHTTPD
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*

class WebServer : NanoHTTPD(8080) {

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


                    "<p style=\"font-family: Arial; font-size: 100;  color: white;\" >На момент: ${
                        dateFormat.format(
                            currentTime
                        )
                    }</p>" +

                    "<b style=\"color: white; font-family: Arial; font-size: 50;\">" +

                        "<p>Заряд аккумулятора: $batteryPercent</p>" +
                        "<p>$batteryStatus</p>" +

                        "<p>$chargingCurrent</p>" +

                        "<p>Температура акб: $batteryTemp º</p>" +
                        "<p>Температура сокета: $cpuTemp º</p>" +
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