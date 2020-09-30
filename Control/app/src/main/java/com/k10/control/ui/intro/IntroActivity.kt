package com.k10.control.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.k10.control.R
import com.k10.control.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_intro.*

@AndroidEntryPoint
class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: IntroActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        connect.setOnClickListener(this)

        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.socketStatus().observe(this) {
            if (it.isConnected) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.connect -> {
                connectSocket()
            }
        }
    }

    private fun connectSocket() {
        var iptext = ipAddress.text.toString()
        iptext = iptext.trim()

        var IPAddress = ""
        var portNumber = 5000

        var porttext = port.text.toString()
        porttext = porttext.trim()

        var isIPOk = false
        var isPortOk = false
        //check ip string
        if (iptext.isEmpty()) {
            ipAddress.error = "Please Enter IP address"
        } else {
            if (iptext.length < 11) {
                ipAddress.error = "Wrong IP address"
            } else {
                isIPOk = true
                IPAddress = iptext
            }
        }
        //check port validation
        if (porttext.isBlank()) {
            port.error = "Please Enter Port number"
        } else {
            try {
                isPortOk = true
                portNumber = porttext.toInt()
            } catch (e: Exception) {
                port.error = "Wrong Port Number"
            }
        }
        //return if neither is OK
        if (!isIPOk || !isPortOk) {
            return
        }

        viewModel.connect(IPAddress, portNumber)

    }
}