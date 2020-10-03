package com.k10.control.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository

class MainActivityViewModel
@ViewModelInject constructor(
    private val repository: Repository
) :
    ViewModel() {

    fun socketStatus() = repository.getSocketStatusLiveData()

    fun passwordSet() = repository.passwordSet

    suspend fun closeConnection(){
        repository.closeConnection()
    }

    suspend fun sendPassword(password: String){
        repository.sendPassword(password)
    }
}