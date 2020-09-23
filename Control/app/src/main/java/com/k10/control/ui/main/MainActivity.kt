package com.k10.control.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.k10.control.R
import com.k10.control.network.Repository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener(this)
        start.setOnClickListener(this)

        subscribeObserver()
    }

    private fun subscribeObserver(){
        repository.socketStatus.observe(this){
            text.text = it.message
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.button -> {
                CoroutineScope(IO).launch {
                    repository.sendPassword("password")
                }
            }
            R.id.start -> {
                val ipaddress = ipadd.text.toString()
                CoroutineScope(IO).launch {
                    repository.startConnection(ipaddress, 5000)
                }
            }
        }
    }
}