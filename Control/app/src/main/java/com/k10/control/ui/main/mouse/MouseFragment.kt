package com.k10.control.ui.main.mouse

import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.k10.control.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mouse.*

@AndroidEntryPoint
class MouseFragment : Fragment(R.layout.fragment_mouse), View.OnClickListener, View.OnTouchListener {

    private val viewModel: MouseFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rightClick.setOnClickListener(this)
        leftClick.setOnClickListener(this)

        trackPad.setOnTouchListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id!!) {
            R.id.leftClick -> {
                viewModel.leftClick()
            }
            R.id.rightClick -> {
                viewModel.rightClick()
            }
        }
    }

    private var startX = 0f
    private var startY = 0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                val moveY = event.y - startY
                val moveX = event.x - startX
                startX = event.x
                startY = event.y
                viewModel.pointerMoveBy(moveX, moveY)
            }
        }
        return true
    }
}