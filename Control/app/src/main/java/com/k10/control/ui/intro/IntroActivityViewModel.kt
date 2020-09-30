package com.k10.control.ui.intro

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class IntroActivityViewModel
@ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun connect(ipAddress: String, port: Int){
        CoroutineScope(IO).launch {
            repository.startConnection(ipAddress, port)
        }
    }

    fun socketStatus() = repository.getSocketStatusLiveData()

}