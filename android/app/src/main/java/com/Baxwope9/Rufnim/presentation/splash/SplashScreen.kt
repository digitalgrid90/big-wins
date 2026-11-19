package com.Baxwope9.Rufnim.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.di.ServiceLocator
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.NavyDeep
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.core.ui.ScreenBackground

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val splashViewModel: SplashViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val finished by splashViewModel.finished.collectAsStateWithLifecycle()

    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val logoAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "logoAlpha"
    )
    val logoScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.92f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "logoScale"
    )

    val loop = rememberInfiniteTransition(label = "splashLoop")
    val glow by loop.animateFloat(
        initialValue = 0.18f,
        targetValue = 0.42f,
        animationSpec = infiniteRepeatable(tween(2600, easing = LinearEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val sweep by loop.animateFloat(
        initialValue = -0.3f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(tween(3200, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )

    ScreenBackground(
        overlay = listOf(
            NavyDeep.copy(alpha = 0.95f),
            NavyDeep.copy(alpha = 0.96f),
            NavyDeep.copy(alpha = 0.98f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Saffron.copy(alpha = glow), NavyDeep.copy(alpha = 0f)),
            radius = 520f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(168.dp)
                    .border(1.dp, Saffron.copy(alpha = 0.34f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(132.dp)
                        .alpha(logoAlpha)
                        .scale(logoScale)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.brand_title),
                style = MaterialTheme.typography.displaySmall,
                letterSpacing = 8.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(1.dp)
                    .background(Saffron)
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.brand_subtitle),
                style = MaterialTheme.typography.labelSmall,
                letterSpacing = 4.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(44.dp))
            SweepLine(progress = sweep)
            Spacer(modifier = Modifier.height(44.dp))
            LinearProgressIndicator(
                color = Saffron,
                trackColor = Hairline,
                modifier = Modifier
                    .width(160.dp)
                    .height(2.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(36.dp))
            LampRow()
        }
    }
}

@Composable
private fun SweepLine(progress: Float) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Hairline.copy(alpha = 0.6f))
    ) {
        val span = maxWidth
        Box(
            modifier = Modifier
                .offset(x = span * progress)
                .width(span * 0.34f)
                .height(1.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Teal.copy(alpha = 0f),
                            Teal.copy(alpha = 0.5f),
                            Teal.copy(alpha = 0f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun LampRow() {
    val loop = rememberInfiniteTransition(label = "lamps")
    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        repeat(5) { index ->
            val lamp by loop.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.95f,
                animationSpec = infiniteRepeatable(
                    tween(2000, delayMillis = index * 240, easing = LinearEasing),
                    RepeatMode.Reverse
                ),
                label = "lamp"
            )
            Box(
                modifier = Modifier
                    .size(3.dp)
                    .clip(CircleShape)
                    .background(Saffron.copy(alpha = lamp))
            )
        }
    }
}
