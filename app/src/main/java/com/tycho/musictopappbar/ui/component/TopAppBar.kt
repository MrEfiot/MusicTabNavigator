package com.tycho.musictopappbar.ui.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tycho.musictopappbar.ui.navigation.NavScreen
import kotlinx.coroutines.launch

@Composable
fun topAppBar(modifier: Modifier = Modifier): NavScreen {
    var selectedScreen by remember {
        mutableStateOf<NavScreen>(NavScreen.PlaylistScreen)
    }
    val navScreens = remember {
        listOf(
            NavScreen.FavoriteScreen,
            NavScreen.PlaylistScreen,
            NavScreen.TracksScreen,
            NavScreen.AlbumsScreen
        )
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        TopAppBarComponent(
            navScreens = navScreens,
            selectedScreen = selectedScreen,
            onScreenSelected = { screen -> selectedScreen = screen }
        )
    }

    return selectedScreen
}

@Composable
fun TopAppBarComponent(
    modifier: Modifier = Modifier,
    navScreens: List<NavScreen>,
    selectedScreen: NavScreen,
    onScreenSelected: (NavScreen) -> Unit
) {
    val positions = remember { mutableStateMapOf<NavScreen, Float>() }
    val coroutineScope = rememberCoroutineScope()
    val offsetAnimatable = remember { Animatable(0f) }

    val centralPosition = remember { mutableStateOf<Float?>(null) }
    LaunchedEffect(selectedScreen) {
        centralPosition.value?.let { centralPos ->
            positions[selectedScreen]?.let { selectedPos ->
                coroutineScope.launch {
                    offsetAnimatable.animateTo(
                        targetValue = centralPos - selectedPos,
                        animationSpec = TweenSpec(800, delay = 0)
                    )
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                centralPosition.value = layoutCoordinates.size.width / 2f
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .offset { IntOffset(offsetAnimatable.value.toInt(), 0) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            navScreens.forEach { screen ->
                val isSelected = screen == selectedScreen
                val fontSize = if (isSelected) 20.sp else 16.sp

                val scale = if (isSelected) 1.5f else 1f

                TopAppBarTitleComponent(
                    modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) { onScreenSelected(screen) }
                        .weight(if (isSelected) 1.5f else 1f)
                        .padding(horizontal = 8.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            alpha = if (isSelected) 1f else 0.7f
                        )
                        .onGloballyPositioned { coordinates ->
                            positions[screen] = coordinates.boundsInParent().center.x
                        },
                    title = screen.title,
                    fontSize = fontSize
                )
            }
        }
    }
}

@Composable
fun TopAppBarTitleComponent(modifier: Modifier = Modifier, title: String, fontSize: TextUnit) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = fontSize,
            label = "FontSizeAnimated",
            transitionSpec = {
                scaleIn(initialScale = -0.1f, animationSpec = tween(800))
                    .togetherWith(scaleOut(targetScale = 0.1f, animationSpec = tween(300)))
            }
        ) { targetFontSize ->
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = targetFontSize),
            )
        }
    }
}

@Preview
@Composable
fun TopAppBarPreview() {
    topAppBar()
}