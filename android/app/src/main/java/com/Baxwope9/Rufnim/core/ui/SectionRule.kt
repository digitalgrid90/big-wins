package com.Baxwope9.Rufnim.core.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.Saffron

@Composable
fun SectionRule(
    modifier: Modifier = Modifier,
    width: Dp = 56.dp,
    color: Color = Saffron
) {
    var drawn by remember { mutableStateOf(false) }
    val animated by animateDpAsState(
        targetValue = if (drawn) width else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "sectionRule"
    )
    LaunchedEffect(width) { drawn = true }
    Box(
        modifier = modifier
            .width(animated)
            .height(1.dp)
            .background(color)
    )
}

@Composable
fun FullRule(modifier: Modifier = Modifier, color: Color = Hairline) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(color)
    )
}
