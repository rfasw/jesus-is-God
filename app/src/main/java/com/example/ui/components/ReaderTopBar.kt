package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ReadingTheme
import com.example.ui.theme.BookThemeColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(
    title: String,
    currentPageIndex: Int,
    totalPages: Int,
    isBookmarked: Boolean,
    isTtsSpeaking: Boolean,
    readingTheme: ReadingTheme,
    fontScale: Float,
    themeColors: BookThemeColors,
    onOpenToc: () -> Unit,
    onOpenSearch: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleTts: () -> Unit,
    onOpenDevGuide: () -> Unit,
    onSetTheme: (ReadingTheme) -> Unit,
    onIncreaseFont: () -> Unit,
    onDecreaseFont: () -> Unit
) {
    var showAppearancePanel by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColors.surface)
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = themeColors.textPrimary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Page ${currentPageIndex + 1} of $totalPages",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = themeColors.textSecondary
                        )
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = themeColors.surface
            ),
            navigationIcon = {
                IconButton(
                    onClick = onOpenToc,
                    modifier = Modifier.testTag("btn_toc")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Table of Contents",
                        tint = themeColors.accent
                    )
                }
            },
            actions = {
                // Search
                IconButton(
                    onClick = onOpenSearch,
                    modifier = Modifier.testTag("btn_search")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Book",
                        tint = themeColors.textPrimary
                    )
                }

                // Bookmark
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.testTag("btn_bookmark")
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark Page",
                        tint = if (isBookmarked) themeColors.accent else themeColors.textSecondary
                    )
                }

                // TTS Read Aloud
                IconButton(
                    onClick = onToggleTts,
                    modifier = Modifier.testTag("btn_tts")
                ) {
                    Icon(
                        imageVector = if (isTtsSpeaking) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = if (isTtsSpeaking) "Stop Audio Reading" else "Read Aloud",
                        tint = if (isTtsSpeaking) themeColors.accent else themeColors.textSecondary
                    )
                }

                // Reading Appearance (Font & Theme)
                IconButton(
                    onClick = { showAppearancePanel = !showAppearancePanel },
                    modifier = Modifier.testTag("btn_appearance")
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatSize,
                        contentDescription = "Adjust Font and Theme",
                        tint = themeColors.textPrimary
                    )
                }

                // Dev Guide ("Applications needed to develop")
                IconButton(
                    onClick = onOpenDevGuide,
                    modifier = Modifier.testTag("btn_dev_guide")
                ) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Developer Setup Guide",
                        tint = themeColors.accent
                    )
                }
            }
        )

        // Dropdown appearance panel for Font and Theme customization
        AnimatedVisibility(visible = showAppearancePanel) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = themeColors.surfaceVariant,
                tonalElevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Reading Mode & Font Size",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = themeColors.textPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Themes
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            ThemeButton(
                                label = "Parchment",
                                isSelected = readingTheme == ReadingTheme.PARCHMENT,
                                bgColor = Color(0xFFF9F5EC),
                                textColor = Color(0xFF261D16),
                                onClick = { onSetTheme(ReadingTheme.PARCHMENT) }
                            )
                            ThemeButton(
                                label = "Night",
                                isSelected = readingTheme == ReadingTheme.NIGHT,
                                bgColor = Color(0xFF101016),
                                textColor = Color(0xFFEBE6ED),
                                onClick = { onSetTheme(ReadingTheme.NIGHT) }
                            )
                            ThemeButton(
                                label = "Light",
                                isSelected = readingTheme == ReadingTheme.LIGHT,
                                bgColor = Color(0xFFFFFFFF),
                                textColor = Color(0xFF1C1D21),
                                onClick = { onSetTheme(ReadingTheme.LIGHT) }
                            )
                        }

                        // Font sizing
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TextButton(
                                onClick = onDecreaseFont,
                                modifier = Modifier.testTag("btn_font_decrease")
                            ) {
                                Text("A-", fontWeight = FontWeight.Bold, color = themeColors.accent)
                            }
                            Text(
                                text = "${(fontScale * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(color = themeColors.textPrimary)
                            )
                            TextButton(
                                onClick = onIncreaseFont,
                                modifier = Modifier.testTag("btn_font_increase")
                            ) {
                                Text("A+", fontWeight = FontWeight.Bold, color = themeColors.accent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeButton(
    label: String,
    isSelected: Boolean,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
