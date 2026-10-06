package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookContent
import com.example.model.ParallelComparison
import com.example.ui.theme.BookThemeColors

@Composable
fun ComparisonMatrixScreen(
    themeColors: BookThemeColors,
    fontScale: Float,
    selectedComparisonId: String,
    onSelectComparison: (String) -> Unit,
    onPrevPage: () -> Unit,
    onNextPage: () -> Unit,
    currentPageIndex: Int,
    totalPages: Int
) {
    val scrollState = rememberScrollState()
    val chipsScrollState = rememberScrollState()

    val currentComparison: ParallelComparison = BookContent.comparisons.firstOrNull { it.id == selectedComparisonId }
        ?: BookContent.comparisons.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColors.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = themeColors.accent.copy(alpha = 0.15f),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CompareArrows,
                        contentDescription = null,
                        tint = themeColors.accent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PARALLEL VISION COMPARISON",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = themeColors.accent,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            Text(
                text = "What the Prophets Saw vs. What John Saw",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = themeColors.textPrimary,
                    fontSize = (21 * fontScale).sp
                )
            )

            Text(
                text = "Comparing the prophetic Old Testament theophanies with the Apostle John's Gospel and apocalyptic visions of Christ.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = themeColors.textSecondary,
                    fontSize = (14 * fontScale).sp
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
            )

            // Horizontal Selector of Comparisons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipsScrollState)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BookContent.comparisons.forEachIndexed { index, comp ->
                    val isSelected = comp.id == currentComparison.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) themeColors.accent else themeColors.surfaceVariant)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) themeColors.accent else themeColors.border,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onSelectComparison(comp.id) }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "${index + 1}. ${comp.title.take(24)}...",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else themeColors.textPrimary
                            )
                        )
                    }
                }
            }

            // Current Comparison Title Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = currentComparison.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = themeColors.accent,
                            fontSize = (17 * fontScale).sp
                        )
                    )
                }
            }

            // Parallel Comparison: Prophetic Vision (Left / Top)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.border)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = themeColors.accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WHAT THE PROPHET SAW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent
                                )
                            )
                        }

                        Text(
                            text = currentComparison.prophetPassage,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = themeColors.textPrimary
                            )
                        )
                    }

                    Text(
                        text = currentComparison.prophetName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = themeColors.textSecondary,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Text(
                        text = "“${currentComparison.prophetVisionText}”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = themeColors.textPrimary,
                            fontSize = (14.5f * fontScale).sp,
                            lineHeight = (22 * fontScale).sp
                        )
                    )
                }
            }

            // Parallel Comparison: What John Saw (Right / Bottom)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, themeColors.accent)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = themeColors.accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WHAT THE APOSTLE JOHN SAW",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent
                                )
                            )
                        }

                        Text(
                            text = currentComparison.johnPassage,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = themeColors.textPrimary
                            )
                        )
                    }

                    Text(
                        text = "Apostle John (Patmos / Upper Room)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = themeColors.textSecondary,
                            fontStyle = FontStyle.Italic
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    Text(
                        text = "“${currentComparison.johnVisionText}”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = themeColors.textPrimary,
                            fontSize = (14.5f * fontScale).sp,
                            lineHeight = (22 * fontScale).sp
                        )
                    )
                }
            }

            // Shared Symbolism Tags
            Text(
                text = "Shared Divine Markers & Symbols:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = themeColors.textSecondary
                ),
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                currentComparison.sharedSymbolism.forEach { tag ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = themeColors.accent.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = tag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = themeColors.accent,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Theological Significance Card (Why this proves Jesus is God)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, themeColors.accent)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = themeColors.accent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "DIVINITY CONCORDANCE PROOF",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = themeColors.accent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentComparison.theologicalSignificance,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            color = themeColors.textPrimary,
                            fontSize = (14.5f * fontScale).sp,
                            lineHeight = (22 * fontScale).sp
                        )
                    )
                }
            }
        }

        // Bottom Navigation Bar
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
                        .testTag("btn_cmp_prev_page")
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
                        .testTag("btn_cmp_next_page")
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
