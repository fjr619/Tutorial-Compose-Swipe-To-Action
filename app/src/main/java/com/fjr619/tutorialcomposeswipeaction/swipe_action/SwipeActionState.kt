package com.fjr619.tutorialcomposeswipeaction.swipe_action

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberSwipeActionState(
    defaultActionSize: Dp = 80.dp,
    listStartAction: List<DraggableItem> = listOf(),
    listEndAction: List<DraggableItem> = listOf(),
    animationSpec: AnimationSpec<Float> = tween()
): SwipeActionState {
    val density = LocalDensity.current
    return remember {
        SwipeActionState(
            defaultActionSize = defaultActionSize,
            listStartAction = listStartAction,
            listEndAction = listEndAction,
            density = density,
            animationSpec = animationSpec
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
data class SwipeActionState(
    val defaultActionSize: Dp = 80.dp,
    val listStartAction: List<DraggableItem> = listOf(),
    val listEndAction: List<DraggableItem> = listOf(),
    private val density: Density,
    private val animationSpec: AnimationSpec<Float> = tween(),
) {

    private val actionSizePx = with(density) { (defaultActionSize * listStartAction.size).toPx() }
    private val endActionSizePx = with(density) { (defaultActionSize * listEndAction.size).toPx() }

    val maxRevealPx : (DragAnchors) -> Float = {
        if (it == DragAnchors.Start) actionSizePx
        else endActionSizePx
    }

    val anchoredDraggableState = AnchoredDraggableState(
        initialValue = DragAnchors.Center,
        anchors = DraggableAnchors {
            DragAnchors.Start at -actionSizePx
            DragAnchors.Center at 0f
            DragAnchors.End at endActionSizePx
        },
        positionalThreshold = { distance: Float -> distance * 0.5f },
        velocityThreshold = { with(density) { 100.dp.toPx() } },
        animationSpec = tween(),
    )

    val alignment: (DragAnchors) -> Alignment = {
        if (it == DragAnchors.Start) {
            Alignment.CenterStart
        } else {
            Alignment.CenterEnd
        }
    }

    val listAction: (DragAnchors) -> List<DraggableItem> = {
        if (it == DragAnchors.Start) {
            listStartAction
        } else {
            listEndAction
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun reset() {
        anchoredDraggableState.animateTo(
            targetValue = DragAnchors.Center
        )
    }
}