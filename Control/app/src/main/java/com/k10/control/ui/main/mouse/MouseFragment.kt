package com.k10.control.ui.main.mouse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.k10.control.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mouse.*

@AndroidEntryPoint
class MouseFragment : Fragment(R.layout.fragment_mouse), View.OnClickListener {

    private val viewModel: MouseFragmentViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rightClick.setOnClickListener(this)
        leftClick.setOnClickListener(this)

        trackPad.setOnTouchListener(trackListener)
        scrollPad.setOnTouchListener(scrollListener)
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

    private var scrollY = 0f
    @SuppressLint("ClickableViewAccessibility")
    private val scrollListener: View.OnTouchListener = View.OnTouchListener { _, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                scrollY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                val moveY = event.y - scrollY
                scrollY = event.y
                viewModel.scrollBy(moveY)
            }
        }
        true
    }

    private var startX = 0f
    private var startY = 0f

    @SuppressLint("ClickableViewAccessibility")
    private val trackListener: View.OnTouchListener = View.OnTouchListener { _, event ->
        when (event?.action) {
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
        true
    }
}