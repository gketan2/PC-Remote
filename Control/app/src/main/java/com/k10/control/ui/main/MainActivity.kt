package com.k10.control.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.k10.control.R
import com.k10.control.network.wrapper.SocketState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        bottomNavigation.setupWithNavController(menu!!, navController)
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.disconnect -> {
                viewModel.closeConnection()
            }
        }
    }

    private fun connectionClosedDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Closing Connection!!")
            .setMessage("By going back, connection will be closed.")
            .setPositiveButton("OK") { _, _ ->
                viewModel.closeConnection()
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