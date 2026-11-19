package com.Baxwope9.Rufnim.presentation.gameover

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.Baxwope9.Rufnim.core.ui.PrimaryButton
import com.Baxwope9.Rufnim.core.ui.ScreenBackground
import com.Baxwope9.Rufnim.core.ui.SecondaryButton
import com.Baxwope9.Rufnim.core.ui.SectionRule
import com.Baxwope9.Rufnim.domain.model.FestivalOutcome
import com.Baxwope9.Rufnim.domain.model.FestivalResult
import com.Baxwope9.Rufnim.presentation.common.Format
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.gameover.components.LedgerRow

@Composable
fun GameOverScreen(
    result: FestivalResult,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { gameOverViewModel.refresh() }

    GameOverContent(
        result = result,
        career = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    result: FestivalResult,
    career: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val survived = result.outcome == FestivalOutcome.COMPLETED
    val accent = if (survived) Saffron else Hazard
    var entered by remember { mutableStateOf(false) }
    val gradeScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 700f),
        label = "gradeScale"
    )
    LaunchedEffect(result) { entered = true }

    ScreenBackground(
        overlay = listOf(
            NavyCanvas.copy(alpha = 0.90f),
            NavyCanvas.copy(alpha = 0.96f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Saffron.copy(alpha = 0.12f), Color.Transparent),
            radius = 560f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(
                    if (survived) R.string.outcome_completed else R.string.outcome_evacuated
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = accent
            )
            Spacer(modifier = Modifier.height(10.dp))
            SectionRule(width = 56.dp, color = accent)
            Spacer(modifier = Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .border(1.dp, Saffron, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(UiLabels.gradeLabel(result.grade)),
                        style = MaterialTheme.typography.displaySmall,
                        color = Saffron,
                        modifier = Modifier.scale(gradeScale)
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column {
                    Text(
                        text = stringResource(R.string.final_grade),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = Format.score(result.score),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    if (result.newBest) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.new_best),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = Teal
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            LedgerRow(
                label = stringResource(R.string.ledger_satisfaction),
                value = Format.percentValue(result.satisfaction),
                accent = Saffron
            )
            LedgerRow(
                label = stringResource(R.string.ledger_blocks),
                value = Format.ratio(result.blocksDelivered, GameConfig.BLOCKS),
                accent = Teal
            )
            LedgerRow(
                label = stringResource(R.string.ledger_efficiency),
                value = Format.percentValue(result.efficiency),
                accent = Saffron
            )
            LedgerRow(
                label = stringResource(R.string.ledger_incidents),
                value = result.incidentsResolved.toString(),
                accent = Teal
            )
            LedgerRow(
                label = stringResource(R.string.ledger_buzz),
                value = Format.buzz(result.peakBuzz),
                accent = Saffron
            )
            LedgerRow(
                label = stringResource(R.string.ledger_permits),
                value = result.permitsUnspent.toString(),
                accent = Teal
            )
            LedgerRow(
                label = stringResource(R.string.ledger_best),
                value = Format.score(career.bestScore),
                accent = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            ZoneBreakdown(result = result)
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                leadingIcon = R.drawable.ic_play
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.disclaimer),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ZoneBreakdown(result: FestivalResult) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        result.zoneCoverage.forEach { entry ->
            val value = entry.second.coerceIn(0, 100)
            val color = when {
                value >= 100 -> Teal
                value >= 50 -> Saffron
                else -> Hazard
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = stringResource(UiLabels.zoneName(entry.first)),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    letterSpacing = 0.5.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Hairline)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(value / 100f)
                            .height(4.dp)
                            .background(color)
                    )
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = Format.percentValue(value),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = color
                )
            }
        }
    }
}
