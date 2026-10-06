package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BookThemeColors

@Composable
fun DeveloperGuideDialog(
    themeColors: BookThemeColors,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Construction,
                    contentDescription = null,
                    tint = themeColors.accent,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Apps Needed to Develop Android",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.textPrimary
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Welcome to Android Development! To build, test, and publish this 'Jesus is God' app on your personal computer and phone, here are the essential software tools you need to install:",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = themeColors.textPrimary,
                        lineHeight = 18.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // App 1: Android Studio
                ToolItem(
                    stepNumber = "1",
                    toolName = "Android Studio (Required)",
                    description = "The official Google IDE for Android. It contains the code editor, Jetpack Compose live preview, and the Android SDK Manager.",
                    subNote = "Download from developer.android.com/studio. Free for Windows, macOS, and Linux.",
                    icon = Icons.Default.Android,
                    themeColors = themeColors
                )

                Spacer(modifier = Modifier.height(10.dp))

                // App 2: JDK
                ToolItem(
                    stepNumber = "2",
                    toolName = "Java Development Kit (JDK 17 or 21)",
                    description = "Needed to run Gradle builds and compile Kotlin code. The great news: Android Studio already includes a bundled OpenJDK automatically!",
                    subNote = "No separate download is required if you use Android Studio's default bundled JDK.",
                    icon = Icons.Default.Code,
                    themeColors = themeColors
                )

                Spacer(modifier = Modifier.height(10.dp))

                // App 3: Git
                ToolItem(
                    stepNumber = "3",
                    toolName = "Git / GitHub Desktop",
                    description = "Used for version control so you can clone your code repository, back it up to GitHub, or collaborate with others.",
                    subNote = "Download from git-scm.com or desktop.github.com.",
                    icon = Icons.Default.Terminal,
                    themeColors = themeColors
                )

                Spacer(modifier = Modifier.height(10.dp))

                // App 4: Real Phone Testing
                ToolItem(
                    stepNumber = "4",
                    toolName = "Your Physical Android Phone (or Virtual Device)",
                    description = "To test on your real phone: Go to Settings > About Phone > Tap 'Build Number' 7 times to enable Developer Options, then enable 'USB Debugging'.",
                    subNote = "Plug in via USB, hit the green 'Play' button in Android Studio, and it will install directly!",
                    icon = Icons.Default.PhoneAndroid,
                    themeColors = themeColors
                )

                Spacer(modifier = Modifier.height(14.dp))

                // How to export from this studio
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = themeColors.accent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "How to Export This Project",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = themeColors.accent
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "1. Click the top-right Settings/Export menu in Google AI Studio.\n" +
                                    "2. Download the project as a ZIP or push directly to your GitHub repository.\n" +
                                    "3. Open Android Studio > 'Open Existing Project' > select this unzipped folder.\n" +
                                    "4. You can also generate an installable .apk file via 'Build > Build Bundle(s) / APK(s) > Build APK(s)'.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = themeColors.textPrimary,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = themeColors.accent),
                modifier = Modifier.testTag("btn_close_dev_guide")
            ) {
                Text("Got It!")
            }
        },
        containerColor = themeColors.surface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ToolItem(
    stepNumber: String,
    toolName: String,
    description: String,
    subNote: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    themeColors: BookThemeColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = themeColors.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = themeColors.accent.copy(alpha = 0.2f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = themeColors.accent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "$stepNumber. $toolName",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = themeColors.textPrimary
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = themeColors.textPrimary,
                        lineHeight = 16.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subNote,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = themeColors.textSecondary,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                )
            }
        }
    }
}
