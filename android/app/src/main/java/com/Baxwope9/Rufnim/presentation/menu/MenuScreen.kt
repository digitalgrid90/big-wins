package com.Baxwope9.Rufnim.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.di.ServiceLocator
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.NavyCanvas
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.core.ui.FullRule
import com.Baxwope9.Rufnim.core.ui.PrimaryButton
import com.Baxwope9.Rufnim.core.ui.ScreenBackground
import com.Baxwope9.Rufnim.core.ui.SecondaryButton
import com.Baxwope9.Rufnim.core.ui.SectionRule
import com.Baxwope9.Rufnim.core.ui.StatCard
import com.Baxwope9.Rufnim.domain.model.FestivalScale
import com.Baxwope9.Rufnim.presentation.common.Format
import com.Baxwope9.Rufnim.presentation.common.UiLabels

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onUpgrades: () -> Unit,
    onHowToPlay: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { menuViewModel.refresh() }

    MenuContent(
        state = state,
        onPlay = onPlay,
        onUpgrades = onUpgrades,
        onHowToPlay = onHowToPlay,
        onScale = menuViewModel::selectScale
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onUpgrades: () -> Unit,
    onHowToPlay: () -> Unit,
    onScale: (FestivalScale) -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            NavyCanvas.copy(alpha = 0.62f),
            NavyCanvas.copy(alpha = 0.80f),
            NavyCanvas.copy(alpha = 0.94f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Teal.copy(alpha = 0.10f), Color.Transparent),
            radius = 640f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.icon_home),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .padding(horizontal = 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.menu_best, Format.score(state.bestScore)),
                        style = MaterialTheme.typography.labelSmall,
                        color = Saffron
                    )
                }
            }
            FullRule()
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.brand_line_one),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary
            )
            Text(
                text = stringResource(R.string.brand_line_two),
                style = MaterialTheme.typography.displaySmall,
                color = Saffron
            )
            Spacer(modifier = Modifier.height(12.dp))
            SectionRule(width = 48.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 60.dp, top = 8.dp, bottom = 8.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FestivalScale.entries.forEach { scale ->
                    ScaleChip(
                        label = stringResource(UiLabels.scaleLabel(scale)),
                        selected = state.scale == scale,
                        selectedDescription = stringResource(R.string.cd_selected),
                        idleDescription = stringResource(R.string.cd_not_selected),
                        onPick = { onScale(scale) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.menu_hint),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                leadingIcon = R.drawable.ic_play
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SecondaryButton(
                    text = stringResource(R.string.upgrades),
                    onClick = onUpgrades,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.how_to_play),
                    onClick = onHowToPlay,
                    modifier = Modifier.weight(1f)
                )
            }
            if (state.festivalsPlayed > 0 && state.bestGrade != null) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_festivals),
                        value = state.festivalsPlayed.toString(),
                        accent = Teal,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_best_grade),
                        value = stringResource(UiLabels.gradeLabel(state.bestGrade)),
                        accent = Saffron,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.disclaimer),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = TextMuted,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun ScaleChip(
    label: String,
    selected: Boolean,
    selectedDescription: String,
    idleDescription: String,
    onPick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onPick,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, if (selected) Saffron else Hairline),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) Saffron.copy(alpha = 0.14f) else Color.Transparent,
            contentColor = if (selected) Saffron else TextSecondary
        ),
        modifier = modifier
            .height(48.dp)
            .semantics {
                stateDescription = if (selected) selectedDescription else idleDescription
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            color = if (selected) Saffron else TextSecondary
        )
    }
}
