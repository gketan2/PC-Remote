package com.k10.control.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository
import com.k10.control.request.Services
import com.k10.control.utils.KeyTypeValuePair
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivityViewModel
@ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun socketStatus() = repository.getSocketStatusLiveData()

    fun closeConnection() {
        CoroutineScope(IO).launch {
            repository.closeConnection()
        }
    }

    /**
     * MOUSE FRAGMENT RELATED THINGS
     */
    var trackScale: Int = 1

    fun leftClick() {
        CoroutineScope(IO).launch {
            repository.mouseClick(Services.C_MOUSE_LEFT_CLICK)
        }
    }

    fun rightClick() {
        CoroutineScope(IO).launch {
            repository.mouseClick(Services.C_MOUSE_RIGHT_CLICK)
        }
    }

    fun forward() {
        CoroutineScope(IO).launch {
            repository.mouseClick(Services.C_MOUSE_FORWARD)
        }
    }

    fun back() {
        CoroutineScope(IO).launch {
            repository.mouseClick(Services.C_MOUSE_BACK)
        }
    }

    fun scrollUp() {
        CoroutineScope(IO).launch {
            repository.scroll(Services.C_MOUSE_SCROLL_UP)
        }
    }

    fun scrollDown() {
        CoroutineScope(IO).launch {
            repository.scroll(Services.C_MOUSE_SCROLL_DOWN)
        }
    }

    fun scrollLeft() {
        CoroutineScope(IO).launch {
            repository.scroll(Services.C_MOUSE_SCROLL_LEFT)
        }
    }

    fun scrollRight() {
        CoroutineScope(IO).launch {
            repository.scroll(Services.C_MOUSE_SCROLL_RIGHT)
        }
    }

    fun pointerMoveBy(x: Float, y: Float) {
        CoroutineScope(IO).launch {
            repository.movePointerBy(x.toInt() / trackScale, y.toInt() / trackScale)
        }
    }

    /**
     * KEYBOARD FRAGMENT
     */
    private val specialKeys: ArrayList<KeyTypeValuePair> = ArrayList()
    private val hotKeys: ArrayList<KeyTypeValuePair> = ArrayList()

    fun typeString(text: String) {
        CoroutineScope(IO).launch {
            repository.typeString(text)
        }
    }

    fun addInSpecialArray(type: Int, value: String) {
        specialKeys.add(KeyTypeValuePair(type, value))
    }

    fun sendSpecialKeys() {
        CoroutineScope(IO).launch {
            val copyList: ArrayList<KeyTypeValuePair> = ArrayList()
            copyList.addAll(specialKeys)
            repository.sendSpecialKeys(specialKeys)
            specialKeys.clear()
        }
    }

    fun addInHotKeyArray(type: Int, value: String) {
        hotKeys.add(KeyTypeValuePair(type, value))
    }

    fun sendHotKeys() {
        CoroutineScope(IO).launch {
            repository.sendHotKeys(specialKeys)
            specialKeys.clear()
        }
    }
}