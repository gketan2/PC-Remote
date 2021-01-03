package com.k10.control.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.k10.control.R
import com.k10.control.network.wrapper.SocketState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import studio.carbonylgroup.textfieldboxes.ExtendedEditText

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //BottomNav library doesn't work without actionbar
        //since I don't want actionBar in my view, therefore hiding
        supportActionBar?.hide()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController

        setPassword.setOnClickListener(this)
        disconnect.setOnClickListener(this)

        //observing socket status and updating in textview
        viewModel.socketStatus().observe(this) {
            connStatus.text = "Status: ${it.message}"
            when (it.state) {
                SocketState.CONNECTED -> {
                    ipAddPort.text = "${it.ip}:${it.port}"
                }
                else -> {
                    finish()
                }
            }
        }

        //password status
        viewModel.passwordSet().observe(this) {
            when {
                it == null -> {
                    passwordStatus.text = "Password Not Set"
                }
                it.isEmpty() -> {
                    passwordStatus.text = "Password Not Set"
                }
                else -> {
                    passwordStatus.text = "Set Password: '${it}'"
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        bottomNavigation.setupWithNavController(menu!!, navController)
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.setPassword -> {
                //dialog to accept password
                passwordDialog()
            }
            R.id.disconnect -> {
                CoroutineScope(IO).launch {
                    viewModel.closeConnection()
                }
            }
        }
    }

    /**
     * Creates a dialog which accepts password.
     *
     * The password is sent to server.
     */
    private fun passwordDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_password)
        val button = dialog.findViewById<MaterialButton>(R.id.passwordFragment_confirm)
        val passwordField = dialog.findViewById<ExtendedEditText>(R.id.passwordFragment_field)

        button.setOnClickListener {
            val password = passwordField.text.toString()
            if (password.isNotEmpty()) {
                CoroutineScope(IO).launch {
                    viewModel.sendPassword(password)
                }
                dialog.dismiss()
            } else {
                passwordField.error = "Please Enter Password"
            }
        }

        dialog.show()
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    private fun connectionClosedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Closing Connection!!")
            .setMessage("By going back, connection will be closed.")
            .setPositiveButton("OK") { _, _ ->
                CoroutineScope(IO).launch {
                    viewModel.closeConnection()
                }
                finish()
            }
            .setNeutralButton("Cancel") { _, _ ->
            }
            .show()
    }

    override fun onBackPressed() {
        connectionClosedDialog()
    }
}