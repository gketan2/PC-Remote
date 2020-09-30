package com.k10.control.ui.main.mouse

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.k10.control.network.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MouseFragmentViewModel
@ViewModelInject constructor(
    private val repository: Repository
) : ViewModel() {

    fun leftClick() {
        CoroutineScope(IO).launch {
            repository.leftClick()
        }
    }

    fun rightClick() {
        CoroutineScope(IO).launch {
            repository.rightClick()
        }
    }

    fun doubleClick() {
        CoroutineScope(IO).launch {
            repository.doubleClick()
        }
    }

    fun pointerMoveBy(x: Float, y: Float) {
        CoroutineScope(IO).launch {
            repository.movePointerBy(x, y)
        }
        println("move pointer by: x=$x, $y")
    }
}