package com.k10.control.ui.intro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.k10.control.R
import com.k10.control.network.wrapper.SocketState
import com.k10.control.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_intro.*

@AndroidEntryPoint
class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: IntroActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        //handling screen rotation
        savedInstanceState?.let {
            if (it.containsKey("ip"))
                ipAddressField.setText(it.getString("ip"))
            if (it.containsKey("port"))
                portField.setText(it.getString("port"))
        }

        connect.setOnClickListener(this)

        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.socketStatus().observe(this) {
            status.text = it.message
            if (isShowingConnectingDialog) {
                connectingDialog?.dismiss()
                isShowingConnectingDialog = false
                connectingDialog = null
            }
            when (it.state) {
                SocketState.CONNECTED -> {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                }
                SocketState.DISCONNECTED -> {
                }
                SocketState.CONNECTING -> {
                    //SHOW DIALOG
                    showConnectingDialog(it.ip, it.port)
                }
                SocketState.FAILED -> {
                    //status.setTextColor(0xFF0000)
                }
            }
        }
    }

    //handling screen rotation
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("ip", ipAddressField.text.toString())
        outState.putString("port", portField.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.connect -> {
                connectSocket()
            }
        }
    }

    private fun connectSocket() {
        var ip = ipAddressField.text.toString()
        ip = ip.trim()

        var port = 5000

        var porttext = portField.text.toString()
        porttext = porttext.trim()

        var isIPOk = false
        var isPortOk = false
        //check ip string
        if (ip.isEmpty()) {
            ipAddressField.error = "Please Enter IP address"
        } else {
            isIPOk = true
        }
        //check port validation
        if (porttext.isBlank()) {
            portField.error = "Please Enter Port number"
        } else {
            try {
                isPortOk = true
                port = porttext.toInt()
            } catch (e: Exception) {
                portField.error = "Wrong Port Number"
            }
        }
        //return if neither is OK
        if (!isIPOk || !isPortOk) {
            return
        }

        viewModel.connect(ip, port)

    }

    private var connectingDialog: AlertDialog? = null
    private var isShowingConnectingDialog = false
    private fun showConnectingDialog(ip: String, port: Int) {
        connectingDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Connecting...")
            .setMessage("Connecting to $ip:$port")
            .show()
        isShowingConnectingDialog = true
    }
}