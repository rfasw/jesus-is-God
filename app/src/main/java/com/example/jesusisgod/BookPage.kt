package com.example.jesusisgod

sealed class BookPage {
    data class Cover(val imageRes: Int) : BookPage()
    data class End(val imageRes: Int) : BookPage()
    data class TextPage(val content: String) : BookPage()
}
