package com.k10.control.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository
import com.k10.control.utils.KeyTypeValuePair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel
@ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun socketStatus() = repository.getSocketStatusLiveData()

    fun passwordSet() = repository.passwordSet

    suspend fun closeConnection() {
        repository.closeConnection()
    }

    suspend fun sendPassword(password: String) {
        repository.sendPassword(password)
    }

    /**
     * MOUSE FRAGMENT RELATED THINGS
     */
    var scrollScale: Int = 50
    var trackScale: Int = 1

    fun leftClick() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.leftClick()
        }
    }

    fun rightClick() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.rightClick()
        }
    }

    fun doubleClick() {
        CoroutineScope(Dispatchers.IO).launch {
            repository.doubleClick()
        }
    }

    fun pointerMoveBy(x: Float, y: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.movePointerBy(x.toInt() / trackScale, y.toInt() / trackScale)
        }
    }

    fun scrollBy(y: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.scrollBy(y / scrollScale)
        }
    }

    /**
     * KEYBOARD FRAGMENT
     */
    private val specialKeys: ArrayList<KeyTypeValuePair> = ArrayList()
    private val hotKeys: ArrayList<KeyTypeValuePair> = ArrayList()

    fun typeString(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.typeString(text)
        }
    }

    fun addInSpecialArray(type: Int, value: String){
        specialKeys.add(KeyTypeValuePair(type, value))
    }

    fun sendSpecialKeys(){
        CoroutineScope(Dispatchers.IO).launch {
            val copyList: ArrayList<KeyTypeValuePair> = ArrayList()
            copyList.addAll(specialKeys)
            repository.sendSpecialKeys(specialKeys)
            specialKeys.clear()
        }
    }

    fun addInHotKeyArray(type: Int, value: String){
        hotKeys.add(KeyTypeValuePair(type, value))
    }

    fun sendHotKeys(){
        CoroutineScope(Dispatchers.IO).launch {
            repository.sendHotKeys(specialKeys)
            specialKeys.clear()
        }
    }
}