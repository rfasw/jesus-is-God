package com.example.model

enum class ReadingTheme {
    PARCHMENT, // Warm ancient scroll / sepia
    NIGHT,     // Deep obsidian / navy dark
    LIGHT      // Crisp classic ivory
}

data class ScriptureVerse(
    val reference: String,
    val text: String,
    val keyHighlight: String? = null,
    val commentaryNote: String? = null
)

data class ParallelComparison(
    val id: String,
    val title: String,
    val prophetName: String,
    val prophetPassage: String,
    val prophetVisionText: String,
    val johnPassage: String,
    val johnVisionText: String,
    val theologicalSignificance: String,
    val sharedSymbolism: List<String>
)

data class BookChapter(
    val id: Int,
    val partTitle: String,
    val title: String,
    val subtitle: String,
    val keyVerseRef: String,
    val keyVerseText: String,
    val bodyParagraphs: List<String>,
    val scriptures: List<ScriptureVerse>,
    val theologicalProofSummary: String,
    val originalLanguageNotes: List<Pair<String, String>> = emptyList() // Word, Meaning
)

data class SearchResult(
    val chapterId: Int,
    val chapterTitle: String,
    val matchedSnippet: String
)
