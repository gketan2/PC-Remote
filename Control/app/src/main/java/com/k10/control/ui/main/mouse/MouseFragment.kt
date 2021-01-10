package com.k10.control.ui.main.mouse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.k10.control.R
import com.k10.control.ui.main.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_mouse.*

@AndroidEntryPoint
class MouseFragment : Fragment(R.layout.fragment_mouse), View.OnClickListener,
    SeekBar.OnSeekBarChangeListener {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rightClick.setOnClickListener(this)
        leftClick.setOnClickListener(this)
        scrollUp.setOnClickListener(this)
        scrollDown.setOnClickListener(this)
        scrollLeft.setOnClickListener(this)
        scrollRight.setOnClickListener(this)
        forwardClick.setOnClickListener(this)
        backClick.setOnClickListener(this)

        trackPad.setOnTouchListener(trackListener)

        trackScaleField.text = viewModel.trackScale.toString()

        trackPadSeek.progress = viewModel.trackScale

        trackPadSeek.setOnSeekBarChangeListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id!!) {
            R.id.leftClick -> {
                viewModel.leftClick()
            }
            R.id.rightClick -> {
                viewModel.rightClick()
            }
            R.id.scrollUp -> {
                viewModel.scrollUp()
            }
            R.id.scrollDown -> {
                viewModel.scrollDown()
            }
            R.id.scrollLeft -> {
                viewModel.scrollLeft()
            }
            R.id.scrollRight -> {
                viewModel.scrollRight()
            }
            R.id.forwardClick -> {
                viewModel.forward()
            }
            R.id.backClick -> {
                viewModel.back()
            }
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        when (seekBar?.id) {
            R.id.trackPadSeek -> {
                viewModel.trackScale = progress
                trackScaleField.text = progress.toString()
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    private var trackSourceX = 0f
    private var trackSourceY = 0f
    private var trackMoveX = 0f
    private var trackMoveY = 0f

    @SuppressLint("ClickableViewAccessibility")
    private val trackListener: View.OnTouchListener = View.OnTouchListener { _, event ->
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                trackSourceX = event.x
                trackSourceY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                trackMoveX = event.x - trackSourceX
                trackMoveY = event.y - trackSourceY
                trackSourceX = event.x
                trackSourceY = event.y
                viewModel.pointerMoveBy(trackMoveX, trackMoveY)
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        true
    }

//    private var scrollSource = 0f
//    private var scrollMoveBy = 0f

//    @SuppressLint("ClickableViewAccessibility")
//    private val scrollListener: View.OnTouchListener = View.OnTouchListener { _, event ->
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> {
//                scrollSource = event.y
//            }
//            MotionEvent.ACTION_MOVE -> {
//                scrollMoveBy = event.y - scrollSource
//                scrollSource = event.y
//                viewModel.scrollBy(scrollMoveBy)
//            }
//            MotionEvent.ACTION_UP -> {
//            }
//        }
//        true
//    }
}