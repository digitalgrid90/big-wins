package com.Baxwope9.Rufnim.presentation.upgrades

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.di.ServiceLocator
import com.Baxwope9.Rufnim.core.theme.NavyCanvas
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.ui.FullRule
import com.Baxwope9.Rufnim.core.ui.ScreenBackground
import com.Baxwope9.Rufnim.core.ui.SecondaryButton
import com.Baxwope9.Rufnim.core.ui.SectionRule
import com.Baxwope9.Rufnim.domain.model.UpgradeBranch
import com.Baxwope9.Rufnim.domain.model.UpgradeId
import com.Baxwope9.Rufnim.domain.usecase.PurchaseResult
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.upgrades.components.UpgradeNodeCard

@Composable
fun UpgradesScreen(
    fromGame: Boolean,
    onBack: () -> Unit
) {
    val upgradesViewModel: UpgradesViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by upgradesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { upgradesViewModel.refresh() }

    UpgradesContent(
        state = state,
        fromGame = fromGame,
        onBuy = upgradesViewModel::buy,
        onBack = onBack
    )
}

@Composable
private fun UpgradesContent(
    state: UpgradesUiState,
    fromGame: Boolean,
    onBuy: (UpgradeId) -> Unit,
    onBack: () -> Unit
) {
    ScreenBackground(
        overlay = listOf(
            NavyCanvas.copy(alpha = 0.88f),
            NavyCanvas.copy(alpha = 0.96f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Teal.copy(alpha = 0.09f), Color.Transparent),
            radius = 600f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.upgrades),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.hud_permits, state.permits),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 16.sp,
                    color = Saffron
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SectionRule(width = 52.dp)
            Spacer(modifier = Modifier.height(6.dp))
            FullRule()
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                UpgradeBranch.entries.forEach { branch ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(UiLabels.branchSprite(branch)),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            text = stringResource(UiLabels.branchName(branch)),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center,
                            color = Teal
                        )
                        state.nodes.filter { it.branch == branch }.forEach { node ->
                            UpgradeNodeCard(
                                node = node,
                                affordable = node.cost <= state.permits,
                                onBuy = { onBuy(node.id) },
                                onBlocked = { onBuy(node.id) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            NoticeLine(state = state)
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(if (fromGame) R.string.back else R.string.menu),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun NoticeLine(state: UpgradesUiState) {
    val message = when {
        state.notice == PurchaseResult.LOCKED -> R.string.notice_locked
        state.notice == PurchaseResult.TOO_EXPENSIVE -> R.string.notice_expensive
        state.notice == PurchaseResult.ALREADY_OWNED -> R.string.notice_owned
        state.permits == 0 && !state.anyAffordable -> R.string.notice_no_permits
        else -> null
    }
    if (message == null) return
    Text(
        text = stringResource(message),
        style = MaterialTheme.typography.labelSmall,
        fontSize = 10.sp,
        textAlign = TextAlign.Center,
        color = TextMuted,
        modifier = Modifier.fillMaxWidth()
    )
}
