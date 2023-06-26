package com.pompast.state

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pompast.state.server.WebServer


class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val serviceIntent = Intent(this, ServerService::class.java)
        startService(serviceIntent)

        val textView: TextView = findViewById(R.id.textView)
        textView.text = textView.text.toString() + "\nip:${WebServer(this).getIPAddress()}:8080"


    }






}