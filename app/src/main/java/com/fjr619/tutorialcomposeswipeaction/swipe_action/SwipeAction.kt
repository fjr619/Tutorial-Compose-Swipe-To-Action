package com.fjr619.tutorialcomposeswipeaction.swipe_action

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.SwipeAction(
    modifier: Modifier = Modifier,
    defaultActionSize: Dp = 80.dp,
    parentHeight: Int,
    content: @Composable BoxScope.() -> Unit,
    listStartAction: List<DraggableItem>,
    listEndAction: List<DraggableItem>,
) {
    val density = LocalDensity.current
    val actionSizePx = with(density) { (defaultActionSize * listStartAction.size).toPx() }
    val endActionSizePx = with(density) { (defaultActionSize * listEndAction.size).toPx() }

    val state = remember {
        AnchoredDraggableState(
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
    }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .animateContentSize()
            .animateItemPlacement()
    ) {

        ActionItem(
            state = state,
            alignment = Alignment.CenterStart,
            defaultActionSize = defaultActionSize,
            parentHeight = parentHeight,
            listAction = listStartAction
        )

        ActionItem(
            state = state,
            alignment = Alignment.CenterEnd,
            defaultActionSize = defaultActionSize,
            parentHeight = parentHeight,
            listAction = listEndAction
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(state, Orientation.Horizontal, reverseDirection = true),
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.ActionItem(
    modifier: Modifier = Modifier,
    state: AnchoredDraggableState<DragAnchors>,
    alignment: Alignment,
    defaultActionSize: Dp,
    parentHeight: Int,
    listAction: List<DraggableItem>,
) {
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .align(alignment)
            .then(
                with(LocalDensity.current) {
                    Modifier.height(
                        parentHeight.toDp()
                    )
                }
            )
    ) {
        listAction.forEach { item ->
            Column(
                modifier = modifier
                    .width(defaultActionSize)
                    .fillMaxHeight()
                    .background(item.containerColor)
                    .clickable {
                        if (item.closeOnBackgroundClick) {
                            coroutineScope.launch {
                                state.animateTo(DragAnchors.Center)
                            }
                        }

                        item.onClick()
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(item.iconSize),
                    imageVector = item.icon,
                    contentDescription = item.contentDescription,
                    tint = item.iconColor
                )

                Text(
                    text = item.text,
                    color = item.textColor,
                    fontSize = item.textSize,
                )
            }
        }
    }
}