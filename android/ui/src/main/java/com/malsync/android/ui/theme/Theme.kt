package com.malsync.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBrand,
    onPrimary = TextWhite,
    primaryContainer = DeepDarkSurface,
    onPrimaryContainer = TextWhite,
    secondary = SecondaryBrand,
    onSecondary = TextDark,
    tertiary = TertiaryBrand,
    background = DeepDarkBackground,
    onBackground = TextWhite,
    surface = DeepDarkSurface,
    onSurface = TextWhite,
    surfaceVariant = ElevaredSurface,
    onSurfaceVariant = TextGrey,
    error = StatusDropped,
    outline = GlassBorder
)

// We define Light Color Scheme but force Dark in this specific "AnymeX" request context usually,
// but let's keep it sane for system compliance, just pointing to similar colors or standard light keys.
// For "AnymeX" aesthetic, we heavily prefer Dark.
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrand,
    onPrimary = TextWhite,
    background = Color(0xFFF5F5F7),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun MALSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Force our brand colors by default for that "AnymeX" look
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Transparent status bar for edge-to-edge
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Ensure Type.kt exists or use default
        content = content
    )
}

// Glassmorphism Modifier
// Note: Real backdrop blur requires Android 12+ RenderEffect or a library like Haze.
// This is a stylistic approximation consistent with "AnymeX" utilizing semi-transparent surfaces and borders.
fun Modifier.glassBackground(
    shape: Shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    color: Color = GlassSurface,
    borderColor: Color = GlassBorder
): Modifier = this
    .clip(shape)
    .background(color, shape)
    .border(1.dp, borderColor, shape)
