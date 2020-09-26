package com.k10.control.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.k10.control.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : FragmentActivity(), View.OnClickListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPagerAdapter = ViewPagerAdapter(this)

        viewPager.adapter = viewPagerAdapter
    }

    override fun onClick(v: View?) {
    }
}