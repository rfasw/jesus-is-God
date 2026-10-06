package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookContent
import com.example.ui.theme.BookThemeColors

@Composable
fun TableOfContentsDialog(
    currentPageIndex: Int,
    bookmarkedPages: Set<Int>,
    themeColors: BookThemeColors,
    onSelectPage: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = themeColors.accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Table of Contents",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.textPrimary
                    )
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Item 0: Front Cover
                item {
                    TocEntry(
                        pageIndex = 0,
                        title = "1. Front Cover (seba-cover.png)",
                        subtitle = "Title, Authorship, and Theological Preface",
                        isSelected = currentPageIndex == 0,
                        isBookmarked = bookmarkedPages.contains(0),
                        themeColors = themeColors,
                        onClick = { onSelectPage(0) }
                    )
                }

                item {
                    HorizontalDivider(
                        color = themeColors.border.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Chapters 1 to 14
                items(BookContent.chapters) { chapter ->
                    val pageIdx = chapter.id // 1..14
                    TocEntry(
                        pageIndex = pageIdx,
                        title = "${pageIdx + 1}. Ch ${chapter.id}: ${chapter.title}",
                        subtitle = chapter.subtitle,
                        isSelected = currentPageIndex == pageIdx,
                        isBookmarked = bookmarkedPages.contains(pageIdx),
                        themeColors = themeColors,
                        onClick = { onSelectPage(pageIdx) }
                    )
                }

                item {
                    HorizontalDivider(
                        color = themeColors.border.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Comparisons Matrix
                item {
                    TocEntry(
                        pageIndex = 15,
                        title = "16. Vision Comparisons Matrix",
                        subtitle = "What John Saw vs. What Prophets Saw Side-by-Side",
                        isSelected = currentPageIndex == 15,
                        isBookmarked = bookmarkedPages.contains(15),
                        themeColors = themeColors,
                        onClick = { onSelectPage(15) }
                    )
                }

                // End Epilogue
                item {
                    TocEntry(
                        pageIndex = 16,
                        title = "17. Epilogue & Blessing (seba-end.png)",
                        subtitle = "Closing Doxology, Benediction, and Summary",
                        isSelected = currentPageIndex == 16,
                        isBookmarked = bookmarkedPages.contains(16),
                        themeColors = themeColors,
                        onClick = { onSelectPage(16) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = themeColors.accent, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = themeColors.surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun TocEntry(
    pageIndex: Int,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    isBookmarked: Boolean,
    themeColors: BookThemeColors,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("toc_item_$pageIndex"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) themeColors.accent.copy(alpha = 0.18f) else themeColors.surfaceVariant
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, themeColors.accent) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isSelected) themeColors.accent else themeColors.textPrimary
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = themeColors.textSecondary,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isBookmarked) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Bookmarked",
                        tint = themeColors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (isSelected) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Current Page",
                        tint = themeColors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
