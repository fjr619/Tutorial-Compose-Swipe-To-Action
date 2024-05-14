package com.fjr619.tutorialcomposeswipeaction.swipe_action

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.SwipeActionContainer(
    modifier: Modifier = Modifier,
    parentHeight: Int,
    content: @Composable BoxScope.() -> Unit,
    state: SwipeActionState = rememberSwipeActionState()
) {
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
                ),
            content = content
        )
    }
}

@Composable
fun BoxScope.ActionItem(
    modifier: Modifier = Modifier,
    state: SwipeActionState,
    parentHeight: Int,
    anchor: DragAnchors
) {
    val coroutineScope = rememberCoroutineScope()

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
            Column(
                modifier = modifier
                    .width(state.defaultActionSize)
                    .fillMaxHeight()
                    .background(item.containerColor)
                    .clickable {
                        if (item.closeOnBackgroundClick) {
                            coroutineScope.launch {
                                state.reset()
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

