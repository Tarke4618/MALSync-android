package com.malsync.android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(8.dp), // Standard card
    large = RoundedCornerShape(16.dp), // Glass cards often use larger radius
    extraLarge = RoundedCornerShape(24.dp)
)
