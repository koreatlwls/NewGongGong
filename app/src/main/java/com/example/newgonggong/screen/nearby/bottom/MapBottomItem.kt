package com.example.newgonggong

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MapBotItem(
    text: String,
    imageResource: Int
) {
    val configuration = LocalConfiguration.current
    Box(
        modifier = Modifier
            .size(120.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align(Alignment.BottomCenter)
                .background(color = Color.White)
        ) {
            Image(
                modifier = Modifier
                    .size(35.25.dp)
                    .padding(top = 5.dp)
                    .align(Alignment.TopCenter)
                    .clip(CircleShape),
                painter = painterResource(imageResource),
                contentDescription = "Content Description for Bot Nav Item"
            )
        }
        Box(
            modifier = Modifier
                .height(18.dp)
                .width(70.dp)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White)
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(1.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Start,
                fontSize = 12.sp,
                softWrap = false
            )
        }
    }
}