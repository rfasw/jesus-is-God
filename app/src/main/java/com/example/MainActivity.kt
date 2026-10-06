package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.BookContent
import com.example.ui.BookViewModel
import com.example.ui.components.ReaderTopBar
import com.example.ui.dialogs.BookSearchSheet
import com.example.ui.dialogs.DeveloperGuideDialog
import com.example.ui.dialogs.TableOfContentsDialog
import com.example.ui.screens.ChapterReaderScreen
import com.example.ui.screens.ComparisonMatrixScreen
import com.example.ui.screens.CoverScreen
import com.example.ui.screens.EndScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.getThemeColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: BookViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val themeColors = getThemeColors(uiState.readingTheme)

            // Back handler: return to cover or previous page on system back
            BackHandler(enabled = uiState.currentPageIndex > 0) {
                if (uiState.currentPageIndex == 16 || uiState.currentPageIndex == 15) {
                    viewModel.goToPage(0)
                } else {
                    viewModel.prevPage()
                }
            }

            MyApplicationTheme(readingTheme = uiState.readingTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        val title = when (uiState.currentPageIndex) {
                            0 -> BookContent.BOOK_TITLE
                            in 1..14 -> {
                                val ch = BookContent.chapters.getOrNull(uiState.currentPageIndex - 1)
                                if (ch != null) "Ch ${ch.id}: ${ch.title}" else BookContent.BOOK_TITLE
                            }
                            15 -> "John vs. Prophets Comparisons"
                            else -> "The End — Epilogue & Amen"
                        }

                        ReaderTopBar(
                            title = title,
                            currentPageIndex = uiState.currentPageIndex,
                            totalPages = uiState.totalPages,
                            isBookmarked = uiState.bookmarkedPages.contains(uiState.currentPageIndex),
                            isTtsSpeaking = uiState.isTtsSpeaking,
                            readingTheme = uiState.readingTheme,
                            fontScale = uiState.fontScale,
                            themeColors = themeColors,
                            onOpenToc = { viewModel.showTableOfContents(true) },
                            onOpenSearch = { viewModel.showSearch(true) },
                            onToggleBookmark = { viewModel.toggleBookmark(uiState.currentPageIndex) },
                            onToggleTts = { viewModel.toggleTts() },
                            onOpenDevGuide = { viewModel.showDevGuide(true) },
                            onSetTheme = { viewModel.setReadingTheme(it) },
                            onIncreaseFont = { viewModel.increaseFontSize() },
                            onDecreaseFont = { viewModel.decreaseFontSize() }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(themeColors.background)
                    ) {
                        when (uiState.currentPageIndex) {
                            0 -> {
                                CoverScreen(
                                    themeColors = themeColors,
                                    onStartReading = { viewModel.goToPage(1) },
                                    onOpenToc = { viewModel.showTableOfContents(true) },
                                    onOpenComparisons = { viewModel.goToPage(15) }
                                )
                            }
                            in 1..14 -> {
                                val ch = BookContent.chapters[uiState.currentPageIndex - 1]
                                ChapterReaderScreen(
                                    chapter = ch,
                                    themeColors = themeColors,
                                    fontScale = uiState.fontScale,
                                    currentPageIndex = uiState.currentPageIndex,
                                    totalPages = uiState.totalPages,
                                    onNextPage = { viewModel.nextPage() },
                                    onPrevPage = { viewModel.prevPage() }
                                )
                            }
                            15 -> {
                                ComparisonMatrixScreen(
                                    themeColors = themeColors,
                                    fontScale = uiState.fontScale,
                                    selectedComparisonId = uiState.selectedComparisonId,
                                    onSelectComparison = { viewModel.selectComparison(it) },
                                    onPrevPage = { viewModel.prevPage() },
                                    onNextPage = { viewModel.nextPage() },
                                    currentPageIndex = uiState.currentPageIndex,
                                    totalPages = uiState.totalPages
                                )
                            }
                            else -> {
                                EndScreen(
                                    themeColors = themeColors,
                                    onReturnToCover = { viewModel.goToPage(0) },
                                    onOpenToc = { viewModel.showTableOfContents(true) },
                                    onOpenComparisons = { viewModel.goToPage(15) }
                                )
                            }
                        }
                    }

                    // Dialogs
                    if (uiState.showTableOfContents) {
                        TableOfContentsDialog(
                            currentPageIndex = uiState.currentPageIndex,
                            bookmarkedPages = uiState.bookmarkedPages,
                            themeColors = themeColors,
                            onSelectPage = { viewModel.goToPage(it) },
                            onDismiss = { viewModel.showTableOfContents(false) }
                        )
                    }

                    if (uiState.showSearchDialog) {
                        BookSearchSheet(
                            query = uiState.searchQuery,
                            results = uiState.searchResults,
                            themeColors = themeColors,
                            onQueryChange = { viewModel.updateSearchQuery(it) },
                            onSelectChapter = { viewModel.goToPage(it) },
                            onDismiss = { viewModel.showSearch(false) }
                        )
                    }

                    if (uiState.showDevGuideDialog) {
                        DeveloperGuideDialog(
                            themeColors = themeColors,
                            onDismiss = { viewModel.showDevGuide(false) }
                        )
                    }
                }
            }
        }
    }
}
