package com.example.jesusisgod

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class PageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt("page_index") ?: return
        val pages = BookContent.getPages()
        if (index >= pages.size) return
        val page = pages[index]

        val imageView = view.findViewById<ImageView>(R.id.pageImage)
        val scrollView = view.findViewById<ScrollView>(R.id.pageScroll)
        val textView = view.findViewById<TextView>(R.id.pageText)
        val nextButton = view.findViewById<Button>(R.id.nextButton)
        val backButton = view.findViewById<Button>(R.id.backButton)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        when (page) {
            is BookPage.Cover -> {
                imageView.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                nextButton.visibility = View.VISIBLE
                backButton.visibility = View.GONE
                imageView.setImageResource(page.imageRes)
                nextButton.setOnClickListener {
                    viewPager?.currentItem = index + 1
                }
            }
            is BookPage.End -> {
                imageView.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
                nextButton.visibility = View.GONE
                backButton.visibility = View.VISIBLE
                imageView.setImageResource(page.imageRes)
                backButton.setOnClickListener {
                    viewPager?.currentItem = 0
                }
            }
            is BookPage.TextPage -> {
                imageView.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                nextButton.visibility = View.GONE
                backButton.visibility = View.GONE
                textView.text = page.content
            }
        }
    }

    companion object {
        fun newInstance(index: Int): PageFragment {
            val fragment = PageFragment()
            fragment.arguments = Bundle().apply {
                putInt("page_index", index)
            }
            return fragment
        }
    }
}
