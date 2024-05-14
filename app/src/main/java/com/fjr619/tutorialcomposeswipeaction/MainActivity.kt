package com.fjr619.tutorialcomposeswipeaction

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.fjr619.tutorialcomposeswipeaction.swipe_action.DraggableItem
import com.fjr619.tutorialcomposeswipeaction.swipe_action.SwipeActionContainer
import com.fjr619.tutorialcomposeswipeaction.swipe_action.rememberSwipeActionState
import com.fjr619.tutorialcomposeswipeaction.ui.theme.TutorialComposeSwipeActionTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorialComposeSwipeActionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val programmingLanguages = remember {
                        mutableStateListOf(
                            "Kotlin",
                            "Java",
                            "C++",
                            "C#",
                            "JavaScript",
                        )
                    }

                    var size by remember { mutableStateOf(IntSize.Zero) }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = programmingLanguages,
                            key = { it }
                        ) { data ->
                            SwipeActionContainer(
                                modifier = Modifier.animateItemPlacement(),
                                parentHeight = size.height,
                                state = rememberSwipeActionState(
                                    listStartAction = mutableListOf(
                                        DraggableItem(
                                            containerColor = com.fjr619.tutorialcomposeswipeaction.ui.theme.SaveAction,
                                            icon = Icons.Filled.Star,
                                            text = "Save",
                                            onClick = {
                                                println("ini save")
                                            }
                                        )
                                    ),
                                    listEndAction = mutableListOf(
                                        DraggableItem(
                                            containerColor = com.fjr619.tutorialcomposeswipeaction.ui.theme.EditAction,
                                            icon = Icons.Filled.Edit,
                                            text = "Edit",
                                            onClick = {
                                                println("ini edit")
                                            }
                                        ),
                                        DraggableItem(
                                            containerColor = com.fjr619.tutorialcomposeswipeaction.ui.theme.DeleteAction,
                                            icon = Icons.Filled.Delete,
                                            closeOnBackgroundClick = false,
                                            text = "Delete",
                                            onClick = {
                                                programmingLanguages.remove(data)
                                            }
                                        )
                                    )
                                ),

                                content = {
                                    ListItem(
                                        modifier = Modifier
                                            .defaultMinSize(minHeight = 100.dp)
                                            .onSizeChanged {
                                                size = it
                                            },
                                        headlineContent = { Text(text = data) })
                                })
                        }
                    }
                }
            }
        }
    }
}
