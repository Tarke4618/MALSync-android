package com.malsync.android.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// AnymeX / Premium Palette

// Backgrounds
val DeepDarkBackground = Color(0xFF050505) // Almost black
val DeepDarkSurface = Color(0xFF0F1115)    // Very dark grey
val ElevaredSurface = Color(0xFF1A1D24)    // Elevated cards

// Glassmorphism
val GlassSurface = Color(0x331F2229)       // Semi-transparent
val GlassBorder = Color(0x1FFFFFFF)        // Thin white border for glass

// Brand Accents
val PrimaryBrand = Color(0xFF6C63FF)       // Vibrant Purple/Blue
val SecondaryBrand = Color(0xFF00E5FF)     // Cyan Neon
val TertiaryBrand = Color(0xFFFF2E63)      // Hot Pink

// Text
val TextWhite = Color(0xFFFFFFFF)
val TextGrey = Color(0xFF9E9E9E)
val TextDark = Color(0xFF121212)

// Status
val StatusWatching = Color(0xFF00C853)
val StatusCompleted = Color(0xFF2962FF)
val StatusOnHold = Color(0xFFFFAB00)
val StatusDropped = Color(0xFFD50000)
val StatusPlanToWatch = Color(0xFFB0BEC5)

// Gradients
val PremiumGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF6C63FF),
        Color(0xFF00E5FF)
    )
)

val DarkGradient = Brush.verticalGradient(
    )
)

// Provider Colors
val ColorMAL = Color(0xFF2E51A2)
val ColorAniList = Color(0xFF3DB4F2)
val ColorKitsu = Color(0xFFFD755C)
val ColorSimkl = Color(0xFF242424) // Simkl is black/dark
val ColorShikimori = Color(0xFFEA4C89) // Placeholder pink-ish
