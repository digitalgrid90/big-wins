package com.Baxwope9.Rufnim.presentation.game

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.config.GameConfig
import com.Baxwope9.Rufnim.core.di.ServiceLocator
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.Hazard
import com.Baxwope9.Rufnim.core.theme.NavyCanvas
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.core.ui.FullRule
import com.Baxwope9.Rufnim.core.ui.PrimaryButton
import com.Baxwope9.Rufnim.core.ui.ResourceReadout
import com.Baxwope9.Rufnim.core.ui.SatisfactionBar
import com.Baxwope9.Rufnim.core.ui.ScreenBackground
import com.Baxwope9.Rufnim.core.ui.SecondaryButton
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.domain.model.ResourceType
import com.Baxwope9.Rufnim.domain.model.ZoneId
import com.Baxwope9.Rufnim.presentation.common.Format
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.dialog.IncidentDialog
import com.Baxwope9.Rufnim.presentation.dialog.PauseDialog
import com.Baxwope9.Rufnim.presentation.game.components.ResourceStepper
import com.Baxwope9.Rufnim.presentation.game.components.ZoneCard

@Composable
fun GameScreen(
    runId: Int,
    onFinished: (FestivalResult) -> Unit,
    onSpendPermits: () -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(runId) { gameViewModel.start(runId) }
    LaunchedEffect(Unit) {
        gameViewModel.syncPermits()
        gameViewModel.results.collect { onFinished(it) }
    }
    DisposableEffect(Unit) {
        onDispose { gameViewModel.pause() }
    }

    GameContent(
        state = state,
        onSelectZone = gameViewModel::selectZone,
        onIncrease = gameViewModel::increase,
        onDecrease = gameViewModel::decrease,
        onHold = gameViewModel::holdLargest,
        onCommit = gameViewModel::commitBlock,
        onNextBlock = gameViewModel::nextBlock,
        onChooseBranch = gameViewModel::chooseBranch,
        onShowPause = gameViewModel::showPause,
        onHidePause = gameViewModel::hidePause,
        onSpendPermits = onSpendPermits,
        onMenu = onMenu
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onSelectZone: (ZoneId) -> Unit,
    onIncrease: (ResourceType) -> Unit,
    onDecrease: (ResourceType) -> Unit,
    onHold: () -> Unit,
    onCommit: () -> Unit,
    onNextBlock: () -> Unit,
    onChooseBranch: (Int) -> Unit,
    onShowPause: () -> Unit,
    onHidePause: () -> Unit,
    onSpendPermits: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            NavyCanvas.copy(alpha = 0.80f),
            NavyCanvas.copy(alpha = 0.90f),
            NavyCanvas.copy(alpha = 0.97f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Saffron.copy(alpha = 0.08f), Color.Transparent),
            radius = 700f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            GameHeader(state = state, onShowPause = onShowPause)
            Spacer(modifier = Modifier.height(6.dp))
            SatisfactionBar(
                value = state.satisfaction,
                description = stringResource(R.string.cd_satisfaction, state.satisfaction)
            )
            Spacer(modifier = Modifier.height(6.dp))
            MetaRow(state = state)
            Spacer(modifier = Modifier.height(8.dp))
            FullRule()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                ZoneMap(
                    state = state,
                    onSelectZone = onSelectZone,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 10.dp)
                )
                LowSatisfactionPulse(
                    active = state.satisfaction in 1..24,
                    modifier = Modifier.align(Alignment.Center)
                )
                DeltaBadge(
                    delta = state.lastDelta,
                    perfect = state.perfectBlock,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            FullRule()
            Spacer(modifier = Modifier.height(8.dp))
            ResourceStrip(state = state)
            Spacer(modifier = Modifier.height(8.dp))
            when (state.phase) {
                GamePhase.INTERMISSION -> IntermissionPanel(
                    state = state,
                    onSpendPermits = onSpendPermits,
                    onNextBlock = onNextBlock
                )
                else -> PlanningPanel(
                    state = state,
                    onIncrease = onIncrease,
                    onDecrease = onDecrease,
                    onHold = onHold,
                    onCommit = onCommit
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    val incident = state.incident
    if (incident != null) {
        IncidentDialog(incident = incident, onChoose = onChooseBranch)
    }
    if (state.paused) {
        PauseDialog(onResume = onHidePause, onMenu = onMenu)
    }
}

@Composable
private fun GameHeader(state: GameUiState, onShowPause: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        IconButton(onClick = onShowPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_home),
                contentDescription = stringResource(R.string.cd_pause_menu),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(
                    R.string.block_header,
                    stringResource(UiLabels.blockLabel(state.blockIndex)),
                    state.blockIndex + 1,
                    GameConfig.BLOCKS
                ),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = TextSecondary
            )
            Text(
                text = stringResource(
                    R.string.next_weather,
                    stringResource(UiLabels.weatherLabel(state.nextWeather))
                ),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = Teal
            )
        }
        IconButton(onClick = onShowPause, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(R.drawable.icon_pause),
                contentDescription = stringResource(R.string.cd_pause),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun MetaRow(state: GameUiState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.hud_buzz, Format.buzz(state.buzz)),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = Saffron
        )
        Text(
            text = stringResource(R.string.hud_reserve, state.totalReserve),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = Teal
        )
        Text(
            text = stringResource(R.string.hud_permits, state.permits),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun ZoneMap(
    state: GameUiState,
    onSelectZone: (ZoneId) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        val gap = 12.dp
        val byWidth = (maxWidth - gap) / 2
        val byHeight = (maxHeight - gap) / 2 / 0.82f
        val tile = if (byWidth < byHeight) byWidth else byHeight
        val cardHeight = tile * 0.82f
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawLine(
                color = Hairline,
                start = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.2f),
                end = androidx.compose.ui.geometry.Offset(w * 0.88f, h * 0.8f),
                strokeWidth = 1f
            )
            drawLine(
                color = Hairline,
                start = androidx.compose.ui.geometry.Offset(w * 0.88f, h * 0.2f),
                end = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.8f),
                strokeWidth = 1f
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(gap, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            state.zones.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                    pair.forEach { zone ->
                        ZoneCard(
                            zone = zone,
                            selected = state.selectedZone == zone.zone,
                            width = tile,
                            height = cardHeight,
                            onSelect = { onSelectZone(zone.zone) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DeltaBadge(delta: Int?, perfect: Boolean, modifier: Modifier = Modifier) {
    if (delta == null) return
    Column(horizontalAlignment = Alignment.End, modifier = modifier.padding(top = 4.dp)) {
        Text(
            text = Format.delta(delta),
            style = MaterialTheme.typography.titleMedium,
            color = if (delta >= 0) Teal else Hazard
        )
        if (perfect) {
            Text(
                text = stringResource(R.string.full_house),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = Teal
            )
        }
    }
}

@Composable
private fun ResourceStrip(state: GameUiState) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        ResourceType.entries.forEach { resource ->
            ResourceReadout(
                label = stringResource(UiLabels.resourceLabel(resource)),
                value = state.pools[resource] ?: 0,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PlanningPanel(
    state: GameUiState,
    onIncrease: (ResourceType) -> Unit,
    onDecrease: (ResourceType) -> Unit,
    onHold: () -> Unit,
    onCommit: () -> Unit
) {
    val selected = state.zones.firstOrNull { it.zone == state.selectedZone }
    Column(modifier = Modifier.fillMaxWidth()) {
        if (selected != null) {
            selected.demand.entries.filter { it.value > 0 }.forEach { entry ->
                val assigned = selected.assigned[entry.key] ?: 0
                ResourceStepper(
                    label = stringResource(UiLabels.resourceLabel(entry.key)),
                    assigned = assigned,
                    demand = entry.value,
                    canIncrease = state.interactive &&
                        (state.pools[entry.key] ?: 0) > 0 && assigned < entry.value,
                    canDecrease = state.interactive && assigned > 0,
                    onIncrease = { onIncrease(entry.key) },
                    onDecrease = { onDecrease(entry.key) }
                )
            }
            TextButton(
                onClick = onHold,
                enabled = state.interactive,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.hold_reserve),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    color = if (state.interactive) Teal else TextMuted
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        PrimaryButton(
            text = stringResource(R.string.commit_block),
            onClick = onCommit,
            enabled = state.interactive
        )
    }
}

@Composable
private fun IntermissionPanel(
    state: GameUiState,
    onSpendPermits: () -> Unit,
    onNextBlock: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Hairline, RoundedCornerShape(8.dp))
                .background(Color.Transparent, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(
                    R.string.intermission,
                    state.blockIndex + 1,
                    state.permitsEarned
                ),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        SecondaryButton(
            text = stringResource(R.string.spend_permits),
            onClick = onSpendPermits,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        PrimaryButton(
            text = stringResource(R.string.next_block),
            onClick = onNextBlock
        )
    }
}

@Composable
private fun LowSatisfactionPulse(active: Boolean, modifier: Modifier = Modifier) {
    if (!active) return
    val loop = rememberInfiniteTransition(label = "lowPulse")
    val alpha by loop.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.22f,
        animationSpec = infiniteRepeatable(tween(500, easing = LinearEasing), RepeatMode.Reverse),
        label = "lowPulseAlpha"
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Hazard.copy(alpha = alpha))
    )
}
