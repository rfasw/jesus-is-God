package com.example.ui.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SearchResult
import com.example.ui.theme.BookThemeColors

@Composable
fun BookSearchSheet(
    query: String,
    results: List<SearchResult>,
    themeColors: BookThemeColors,
    onQueryChange: (String) -> Unit,
    onSelectChapter: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = themeColors.accent,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search in 'Jesus is God'",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.textPrimary
                    )
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_query"),
                    placeholder = { Text("Search prophet, verse, or keyword...", fontSize = 13.sp) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = themeColors.textSecondary
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = themeColors.textSecondary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (query.trim().length >= 2) {
                    Text(
                        text = "Found ${results.size} matches:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = themeColors.textSecondary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (query.trim().length < 2) {
                        item {
                            Text(
                                text = "Try searching for:\n• Prophets: Isaiah, Daniel, Ezekiel, Zechariah, Micah\n• Sayings: I AM, Father, Alpha, Worship, Forgive\n• Visions: Cloud, Pierced, Throne, Lamb",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.textSecondary,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    } else if (results.isEmpty()) {
                        item {
                            Text(
                                text = "No matches found for '$query'. Try another word or reference.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = themeColors.textSecondary
                                ),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    } else {
                        items(results) { res ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectChapter(res.chapterId) }
                                    .testTag("search_result_${res.chapterId}"),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Chapter ${res.chapterId}: ${res.chapterTitle}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = themeColors.accent
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = res.matchedSnippet,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = themeColors.textPrimary,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
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
