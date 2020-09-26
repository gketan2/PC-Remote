package com.k10.control.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.k10.control.ui.main.keyboard.KeyboardFragment
import com.k10.control.ui.main.mouse.MouseFragment

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
) : FragmentStateAdapter(fragmentActivity) {

    private var mouseFragment: MouseFragment = MouseFragment()
    private var keyboardFragment: KeyboardFragment = KeyboardFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                keyboardFragment
            }
            1 -> {
                mouseFragment
            }
            else -> {
                keyboardFragment
            }
        }
    }

}