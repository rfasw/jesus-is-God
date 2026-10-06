package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookChapter
import com.example.model.ScriptureVerse
import com.example.ui.theme.BookThemeColors

@Composable
fun ChapterReaderScreen(
    chapter: BookChapter,
    themeColors: BookThemeColors,
    fontScale: Float,
    currentPageIndex: Int,
    totalPages: Int,
    onNextPage: () -> Unit,
    onPrevPage: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Reset scroll when chapter changes
    LaunchedEffect(chapter.id) {
        scrollState.scrollTo(0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.background)
    ) {
        // Scrollable reading content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // Part badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = themeColors.accent.copy(alpha = 0.12f),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = chapter.partTitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.accent,
                        letterSpacing = 1.sp
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            // Chapter Title
            Text(
                text = "Chapter ${chapter.id}: ${chapter.title}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = themeColors.textPrimary,
                    fontSize = (22 * fontScale).sp,
                    lineHeight = (28 * fontScale).sp
                )
            )

            // Subtitle
            Text(
                text = chapter.subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontStyle = FontStyle.Italic,
                    color = themeColors.textSecondary,
                    fontSize = (15 * fontScale).sp,
                    lineHeight = (20 * fontScale).sp
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            // Key Scripture Anchor Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.accent.copy(alpha = 0.35f))
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(64.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(themeColors.accent)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = themeColors.accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = chapter.keyVerseRef,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“${chapter.keyVerseText}”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = themeColors.textPrimary,
                                fontSize = (15 * fontScale).sp,
                                lineHeight = (22 * fontScale).sp
                            )
                        )
                    }
                }
            }

            // Body Paragraphs
            chapter.bodyParagraphs.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = themeColors.textPrimary,
                        fontSize = (16 * fontScale).sp,
                        lineHeight = (26 * fontScale).sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Supporting Scriptures Section
            if (chapter.scriptures.isNotEmpty()) {
                Text(
                    text = "Scriptural Proofs & Cross-References",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.textPrimary
                    ),
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )

                chapter.scriptures.forEach { scripture ->
                    ScriptureCard(scripture = scripture, themeColors = themeColors, fontScale = fontScale)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Original Language Word Studies (Greek / Hebrew)
            if (chapter.originalLanguageNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                OriginalLanguageSection(
                    notes = chapter.originalLanguageNotes,
                    themeColors = themeColors,
                    fontScale = fontScale
                )
            }

            // Theological Proof Summary Card
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = themeColors.accent.copy(alpha = 0.12f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, themeColors.accent)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = themeColors.accent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DIVINE PROOF CONCLUSION",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = themeColors.accent,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = chapter.theologicalProofSummary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = themeColors.textPrimary,
                            fontSize = (15 * fontScale).sp,
                            lineHeight = (22 * fontScale).sp
                        )
                    )
                }
            }
        }

        // Bottom Navigation Bar with touch targets >= 48dp
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = themeColors.surface,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPrevPage,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_prev_page")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Previous Page",
                        tint = themeColors.accent
                    )
                }

                Text(
                    text = "Page ${currentPageIndex + 1} of $totalPages",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = themeColors.textSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                IconButton(
                    onClick = onNextPage,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("btn_next_page")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Page",
                        tint = themeColors.accent
                    )
                }
            }
        }
    }
}

@Composable
private fun ScriptureCard(
    scripture: ScriptureVerse,
    themeColors: BookThemeColors,
    fontScale: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scripture.reference,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.accent
                    )
                )
                if (scripture.keyHighlight != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = themeColors.accent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = scripture.keyHighlight,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = themeColors.accent,
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = scripture.text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = themeColors.textPrimary,
                    fontSize = (14.5f * fontScale).sp,
                    lineHeight = (22 * fontScale).sp
                )
            )

            if (scripture.commentaryNote != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = themeColors.textSecondary,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = scripture.commentaryNote,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = themeColors.textSecondary,
                            fontStyle = FontStyle.Italic,
                            fontSize = (13 * fontScale).sp,
                            lineHeight = (18 * fontScale).sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun OriginalLanguageSection(
    notes: List<Pair<String, String>>,
    themeColors: BookThemeColors,
    fontScale: Float
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = themeColors.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = themeColors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Original Language Insights (${notes.size})",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = themeColors.textPrimary
                        )
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = themeColors.textSecondary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    notes.forEach { (word, meaning) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• $word: ",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent,
                                    fontSize = (13 * fontScale).sp
                                )
                            )
                            Text(
                                text = meaning,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.textPrimary,
                                    fontSize = (13 * fontScale).sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
