package com.fjr619.tutorialcomposeswipeaction.swipe_action

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DraggableItem(
    val containerColor: Color,
    val icon: ImageVector,
    val iconColor: Color = Color.White,
    val iconSize: Dp = 22.dp,
    val showText: Boolean = true,
    val text: String = "",
    val textColor: Color = Color.White,
    val textSize: TextUnit = 12.sp,
    val contentDescription: String? = null,
    val closeOnBackgroundClick: Boolean = true,
    val onClick: () -> Unit
) {
}