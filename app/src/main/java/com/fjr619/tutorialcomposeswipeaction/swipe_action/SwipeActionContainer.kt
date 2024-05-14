package com.fjr619.tutorialcomposeswipeaction.swipe_action

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.SwipeActionContainer(
    modifier: Modifier = Modifier,
    parentHeight: Int,
    onContentClick: (() -> Unit)? = null,
    state: SwipeActionState = rememberSwipeActionState(),
    content: @Composable BoxScope.() -> Unit,
    ) {
    val coroutineScope = rememberCoroutineScope()
    val closeOnContentClickHandler = remember(coroutineScope, state) {
        {
            if (state.anchoredDraggableState.targetValue != DragAnchors.Center) {
                coroutineScope.launch {
                    state.reset()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .animateContentSize()
            .animateItemPlacement()
    ) {

        ActionItem(
            state = state,
            parentHeight = parentHeight,
            anchor = DragAnchors.Start
        )

        ActionItem(
            state = state,
            parentHeight = parentHeight,
            anchor = DragAnchors.End
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = -state.anchoredDraggableState
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    state.anchoredDraggableState,
                    Orientation.Horizontal,
                    reverseDirection = true
                )
                .then(
                    if (onContentClick == null) {
                        Modifier.clickable(
                            onClick = closeOnContentClickHandler,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    } else {
                        Modifier.clickable(
                            onClick = {
                                closeOnContentClickHandler()
                                onContentClick()
                            }
                        )
                    }
                )
,
            content = content
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BoxScope.ActionItem(
    modifier: Modifier = Modifier,
    state: SwipeActionState,
    parentHeight: Int,
    anchor: DragAnchors
) {
    val coroutineScope = rememberCoroutineScope()
    val alphaEasing: Easing = CubicBezierEasing(0.4f, 0.4f, 0.17f, 0.9f)
    val maxRevealPx = state.maxRevealPx(anchor)
    val draggedRatio = (state.anchoredDraggableState.offset.absoluteValue / maxRevealPx.absoluteValue).coerceIn(0f, 1f)
    val alpha = alphaEasing.transform(draggedRatio)

    Row(
        modifier = Modifier
            .align(state.alignment(anchor))
            .then(
                with(LocalDensity.current) {
                    Modifier.height(
                        parentHeight.toDp()
                    )
                }
            )
    ) {
        state.listAction(anchor).forEach { item ->
            val animatedContainerColor = if(alpha in 0f..1f ) item.containerColor.copy(
                alpha = alpha
            ) else item.containerColor

            val backgroundStartClick = remember(coroutineScope, state) {
                {
                    if (state.anchoredDraggableState.targetValue == anchor && item.closeOnBackgroundClick) {
                        coroutineScope.launch {
                            state.reset()
                        }
                    }
                    item.onClick()
                }
            }

            Column(
                modifier = modifier
                    .width(state.defaultActionSize)
                    .fillMaxHeight()
                    .background(animatedContainerColor)
                    .clickable(onClick = { backgroundStartClick() }),
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

                AnimatedVisibility(visible = item.showText) {
                    Text(
                        text = item.text,
                        color = item.textColor,
                        fontSize = item.textSize,
                    )
                }
            }
        }
    }
}

