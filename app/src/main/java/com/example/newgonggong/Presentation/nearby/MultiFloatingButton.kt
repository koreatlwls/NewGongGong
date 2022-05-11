package com.example.newgonggong.Presentation.nearby

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MultiFloatingState {
    Expanded,
    Collapsed
}

enum class Identifier {
    Location,
    Card,
    Food,
    Welfare
}

class MinFabItem(
    val icon: Int,
    val label: String,
    val identifier: String
)

@Composable
fun MultiFloatingButton(
    multiFloatingState: MultiFloatingState,
    onMultiFabStateChange: (MultiFloatingState) -> Unit,
    context: Context,
    items: List<MinFabItem>
) {
    val transition = updateTransition(targetState = multiFloatingState, label = "transition")
    val rotate by transition.animateFloat(label = "rotate") {
        if (it == MultiFloatingState.Expanded) 315f else 0f
    }
    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == MultiFloatingState.Expanded) 1f else 0f
    }
    val textShadow by transition.animateDp(
        label = "textShadow",
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == MultiFloatingState.Expanded) 2.dp else 0.dp
    }

    Column(
        horizontalAlignment = Alignment.End
    ) {
        if (transition.currentState == MultiFloatingState.Expanded) {
            items.forEach {
                MinFab(
                    item = it,
                    onMinFabItemClick = { minFabItem ->
                        when (minFabItem.identifier) {
                            Identifier.Card.name -> {
                                Toast.makeText(context, "Card", Toast.LENGTH_SHORT).show()
                            }
                            Identifier.Food.name -> {
                                Toast.makeText(context, "Food", Toast.LENGTH_SHORT).show()
                            }
                            Identifier.Welfare.name -> {
                                Toast.makeText(context, "Welfare", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    alpha = alpha,
                    textShadow = textShadow
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFloatingState.Expanded) {
                        MultiFloatingState.Collapsed
                    } else {
                        MultiFloatingState.Expanded
                    }
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Floating Action Button",
                modifier = Modifier.rotate(rotate)
                    .size(36.dp)
            )
        }
    }
}

@Composable
fun MinFab(
    item: MinFabItem,
    alpha: Float,
    textShadow: Dp,
    showLabel: Boolean = true,
    onMinFabItemClick: (MinFabItem) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = { onMinFabItemClick.invoke(item) })
    ) {
        if (showLabel) {
            Text(
                text = item.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(50)
                        ).value
                    )
                    .shadow(textShadow)
                    .background(Color.White)
                    .padding(5.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))
        }

        Image(
            painterResource(id = item.icon),
            contentDescription = "minFabItem",
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(6.dp)
        )
    }
}