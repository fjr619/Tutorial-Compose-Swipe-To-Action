package com.fjr619.tutorialcomposeswipeaction

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SaveAction(modifier: Modifier) {
    Column(
        modifier = modifier
            .background(com.fjr619.tutorialcomposeswipeaction.ui.theme.SaveAction),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(22.dp),
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = Color.White
        )

        Text(
            text = "Save",
            color = Color.White,
            fontSize = 12.sp,
        )
    }
}


@Composable
fun EditAction(modifier: Modifier) {
    Column(
        modifier = modifier
            .background(com.fjr619.tutorialcomposeswipeaction.ui.theme.EditAction),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(22.dp),
            imageVector = Icons.Filled.Edit,
            contentDescription = null,
            tint = Color.White
        )

        Text(
            text = "Edit",
            color = Color.White,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun DeleteAction(modifier: Modifier) {
    Column(
        modifier = modifier
            .background(com.fjr619.tutorialcomposeswipeaction.ui.theme.DeleteAction),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(22.dp),
            imageVector = Icons.Filled.Delete,
            contentDescription = null,
            tint = Color.White
        )

        Text(
            text = "Delete",
            color = Color.White,
            fontSize = 12.sp,
        )
    }
}