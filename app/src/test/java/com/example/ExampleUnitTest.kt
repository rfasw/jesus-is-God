package com.example

import com.example.model.BookContent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testBookContent_isNotEmpty() {
        assertEquals("Jesus is God", BookContent.BOOK_TITLE)
        assertTrue(BookContent.chapters.isNotEmpty())
        assertEquals(14, BookContent.chapters.size)
        assertTrue(BookContent.comparisons.isNotEmpty())
        assertEquals(6, BookContent.comparisons.size)
    }

    @Test
    fun testChapters_haveValidScripturesAndProofs() {
        BookContent.chapters.forEach { chapter ->
            assertFalse(chapter.title.isBlank())
            assertFalse(chapter.keyVerseRef.isBlank())
            assertFalse(chapter.theologicalProofSummary.isBlank())
            assertTrue(chapter.bodyParagraphs.isNotEmpty())
        }
    }

    @Test
    fun testComparisons_haveBothProphetAndJohnVisions() {
        BookContent.comparisons.forEach { cmp ->
            assertFalse(cmp.prophetVisionText.isBlank())
            assertFalse(cmp.johnVisionText.isBlank())
            assertFalse(cmp.theologicalSignificance.isBlank())
            assertTrue(cmp.sharedSymbolism.isNotEmpty())
        }
    }
}
