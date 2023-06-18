package com.pompast.state

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.util.Log
import android.view.View
import com.pompast.state.client.ClientSendFile
import com.pompast.state.server.WebServer
import java.io.IOException
import java.net.Inet4Address
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permission()

        val serviceIntent = Intent(this, ServerService::class.java)
        startService(serviceIntent)


    }


    private fun permission() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != 15) {
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), 15)
        }

    }




}