package com.example.jesusisgod

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BookPagerAdapter(
    activity: FragmentActivity,
    private val pageCount: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = pageCount

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(position)
    }
}
