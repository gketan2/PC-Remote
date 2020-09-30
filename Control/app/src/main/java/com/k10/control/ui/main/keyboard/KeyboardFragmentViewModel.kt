package com.k10.control.ui.main.keyboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository
import com.k10.control.utils.KeyTypeValuePair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class KeyboardFragmentViewModel
@ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    private val specialKeys: ArrayList<KeyTypeValuePair> = ArrayList()
    private val hotKeys: ArrayList<KeyTypeValuePair> = ArrayList()

    fun typeString(text: String) {
        CoroutineScope(IO).launch {
            repository.typeString(text)
        }
    }

    fun addInSpecialArray(type: Int, value: String){
        specialKeys.add(KeyTypeValuePair(type, value))
    }

    fun sendSpecialKeys(){
        CoroutineScope(IO).launch {
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
        CoroutineScope(IO).launch {
            repository.sendHotKeys(specialKeys)
            specialKeys.clear()
        }
    }

}