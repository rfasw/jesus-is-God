package com.example.ui

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BookChapter
import com.example.model.BookContent
import com.example.model.ParallelComparison
import com.example.model.ReadingTheme
import com.example.model.SearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

data class BookUiState(
    val currentPageIndex: Int = 0, // 0 = Cover, 1..14 = Chapters, 15 = Comparisons, 16 = End
    val totalPages: Int = 17,
    val readingTheme: ReadingTheme = ReadingTheme.PARCHMENT,
    val fontScale: Float = 1.05f,
    val bookmarkedPages: Set<Int> = emptySet(),
    val isTtsSpeaking: Boolean = false,
    val isTtsAvailable: Boolean = false,
    val showTableOfContents: Boolean = false,
    val showSearchDialog: Boolean = false,
    val showDevGuideDialog: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<SearchResult> = emptyList(),
    val selectedComparisonId: String = "cmp_1"
)

class BookViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val prefs = application.getSharedPreferences("jesus_is_god_book_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        BookUiState(
            currentPageIndex = prefs.getInt("saved_page", 0),
            readingTheme = ReadingTheme.valueOf(prefs.getString("saved_theme", ReadingTheme.PARCHMENT.name) ?: ReadingTheme.PARCHMENT.name),
            fontScale = prefs.getFloat("saved_font_scale", 1.05f),
            bookmarkedPages = prefs.getStringSet("saved_bookmarks", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        )
    )
    val uiState: StateFlow<BookUiState> = _uiState.asStateFlow()

    private var tts: TextToSpeech? = null

    init {
        tts = TextToSpeech(application, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            _uiState.update { it.copy(isTtsAvailable = true) }
        }
    }

    fun goToPage(pageIndex: Int) {
        val clamped = pageIndex.coerceIn(0, _uiState.value.totalPages - 1)
        stopTts()
        _uiState.update { it.copy(currentPageIndex = clamped, showTableOfContents = false, showSearchDialog = false) }
        prefs.edit().putInt("saved_page", clamped).apply()
    }

    fun nextPage() {
        if (_uiState.value.currentPageIndex < _uiState.value.totalPages - 1) {
            goToPage(_uiState.value.currentPageIndex + 1)
        }
    }

    fun prevPage() {
        if (_uiState.value.currentPageIndex > 0) {
            goToPage(_uiState.value.currentPageIndex - 1)
        }
    }

    fun setReadingTheme(theme: ReadingTheme) {
        _uiState.update { it.copy(readingTheme = theme) }
        prefs.edit().putString("saved_theme", theme.name).apply()
    }

    fun increaseFontSize() {
        val newScale = (_uiState.value.fontScale + 0.1f).coerceAtMost(1.4f)
        _uiState.update { it.copy(fontScale = newScale) }
        prefs.edit().putFloat("saved_font_scale", newScale).apply()
    }

    fun decreaseFontSize() {
        val newScale = (_uiState.value.fontScale - 0.1f).coerceAtLeast(0.85f)
        _uiState.update { it.copy(fontScale = newScale) }
        prefs.edit().putFloat("saved_font_scale", newScale).apply()
    }

    fun toggleBookmark(pageIndex: Int) {
        val current = _uiState.value.bookmarkedPages.toMutableSet()
        if (current.contains(pageIndex)) {
            current.remove(pageIndex)
        } else {
            current.add(pageIndex)
        }
        _uiState.update { it.copy(bookmarkedPages = current) }
        prefs.edit().putStringSet("saved_bookmarks", current.map { it.toString() }.toSet()).apply()
    }

    fun showTableOfContents(show: Boolean) {
        _uiState.update { it.copy(showTableOfContents = show) }
    }

    fun showSearch(show: Boolean) {
        _uiState.update { it.copy(showSearchDialog = show, searchQuery = if (!show) "" else it.searchQuery) }
    }

    fun showDevGuide(show: Boolean) {
        _uiState.update { it.copy(showDevGuideDialog = show) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.trim().length < 2) {
            _uiState.update { it.copy(searchResults = emptyList()) }
            return
        }
        val q = query.trim().lowercase(Locale.ROOT)
        val results = mutableListOf<SearchResult>()

        BookContent.chapters.forEach { ch ->
            val matchInTitle = ch.title.lowercase(Locale.ROOT).contains(q)
            val matchInSubtitle = ch.subtitle.lowercase(Locale.ROOT).contains(q)
            val matchInKeyVerse = ch.keyVerseText.lowercase(Locale.ROOT).contains(q) || ch.keyVerseRef.lowercase(Locale.ROOT).contains(q)
            val matchingPara = ch.bodyParagraphs.firstOrNull { it.lowercase(Locale.ROOT).contains(q) }
            val matchingScripture = ch.scriptures.firstOrNull { it.text.lowercase(Locale.ROOT).contains(q) || it.reference.lowercase(Locale.ROOT).contains(q) }

            if (matchInTitle || matchInSubtitle || matchInKeyVerse || matchingPara != null || matchingScripture != null) {
                val snippet = when {
                    matchingPara != null -> extractSnippet(matchingPara, q)
                    matchingScripture != null -> "${matchingScripture.reference}: ${matchingScripture.text.take(90)}..."
                    matchInKeyVerse -> "${ch.keyVerseRef}: ${ch.keyVerseText.take(90)}..."
                    else -> ch.subtitle
                }
                results.add(SearchResult(chapterId = ch.id, chapterTitle = ch.title, matchedSnippet = snippet))
            }
        }
        _uiState.update { it.copy(searchResults = results) }
    }

    private fun extractSnippet(text: String, query: String): String {
        val idx = text.lowercase(Locale.ROOT).indexOf(query)
        if (idx == -1) return text.take(100) + "..."
        val start = (idx - 30).coerceAtLeast(0)
        val end = (idx + query.length + 50).coerceAtMost(text.length)
        return (if (start > 0) "..." else "") + text.substring(start, end).trim() + (if (end < text.length) "..." else "")
    }

    fun selectComparison(id: String) {
        _uiState.update { it.copy(selectedComparisonId = id) }
    }

    fun toggleTts() {
        if (_uiState.value.isTtsSpeaking) {
            stopTts()
        } else {
            readCurrentPageAloud()
        }
    }

    private fun readCurrentPageAloud() {
        val page = _uiState.value.currentPageIndex
        val textToSpeak = when {
            page == 0 -> "Jesus is God. Proofs from the Prophets, the Words of Jesus Himself, and Apostolic Visions of John. By Sebastiano Macheso."
            page in 1..14 -> {
                val ch = BookContent.chapters.getOrNull(page - 1)
                if (ch != null) {
                    "Chapter ${ch.id}. ${ch.title}. ${ch.subtitle}. Key Verse: ${ch.keyVerseRef}. ${ch.keyVerseText}. " +
                            ch.bodyParagraphs.joinToString(" ")
                } else ""
            }
            page == 15 -> {
                val cmp = BookContent.comparisons.firstOrNull { it.id == _uiState.value.selectedComparisonId } ?: BookContent.comparisons.first()
                "Comparing what the Prophets saw with what John saw. ${cmp.title}. Prophecy by ${cmp.prophetName}: ${cmp.prophetVisionText}. John's Vision: ${cmp.johnVisionText}. Theological Proof: ${cmp.theologicalSignificance}"
            }
            else -> "The End. Biblical Benediction and Author Epilogue. Amen."
        }

        if (textToSpeak.isNotEmpty()) {
            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "BOOK_TTS_UTTERANCE")
            _uiState.update { it.copy(isTtsSpeaking = true) }
        }
    }

    fun stopTts() {
        tts?.stop()
        _uiState.update { it.copy(isTtsSpeaking = false) }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
