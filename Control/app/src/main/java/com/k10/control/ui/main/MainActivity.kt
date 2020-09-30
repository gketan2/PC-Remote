package com.k10.control.ui.main

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.DialogCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialDialogs
import com.k10.control.R
import com.k10.control.network.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var repository: Repository

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
        //TODO: IP ADDRESS AND CLOSE (IF DISCONNECTED)
        repository.getSocketStatusLiveData().observe(this) {
            connStatus.text = it.message
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        bottomNavigation.setupWithNavController(menu!!, navController)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
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
                    repository.closeConnection()
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
                    repository.sendPassword(password)
                }
                dialog.dismiss()
            } else {
                passwordField.error = "Please Enter Password"
            }
        }

        dialog.show()
        dialog.window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        CoroutineScope(IO).launch {
            repository.closeConnection()
        }
    }
}