package com.example.jesusisgod

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)

        val pages = BookContent.getPages()
        val adapter = BookPagerAdapter(this, pages.size)
        viewPager.adapter = adapter
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}
