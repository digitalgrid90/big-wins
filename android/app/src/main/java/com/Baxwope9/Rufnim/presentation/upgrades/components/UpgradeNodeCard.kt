package com.Baxwope9.Rufnim.presentation.upgrades.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.SurfacePanel
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.upgrades.NodeState
import com.Baxwope9.Rufnim.presentation.upgrades.UpgradeNodeUi

@Composable
fun UpgradeNodeCard(
    node: UpgradeNodeUi,
    affordable: Boolean,
    onBuy: () -> Unit,
    onBlocked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    val borderColor by animateColorAsState(
        targetValue = when (node.state) {
            NodeState.OWNED -> Teal
            NodeState.AVAILABLE -> Saffron
            NodeState.LOCKED -> Hairline
        },
        animationSpec = tween(durationMillis = 420),
        label = "nodeBorder"
    )
    val ownedLabel = stringResource(R.string.cd_owned)
    val availableLabel = stringResource(R.string.cd_available)
    val lockedLabel = stringResource(R.string.cd_locked)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(86.dp)
            .clip(shape)
            .background(SurfacePanel.copy(alpha = 0.9f), shape)
            .border(1.dp, borderColor, shape)
            .clickable { if (node.state == NodeState.AVAILABLE) onBuy() else onBlocked() }
            .semantics {
                stateDescription = when (node.state) {
                    NodeState.OWNED -> ownedLabel
                    NodeState.AVAILABLE -> availableLabel
                    NodeState.LOCKED -> lockedLabel
                }
            }
            .alpha(if (node.state == NodeState.LOCKED) 0.4f else 1f)
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(UiLabels.upgradeName(node.id)),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            letterSpacing = 0.5.sp,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(UiLabels.upgradeEffect(node.id)),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 9.sp,
            letterSpacing = 0.sp,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (node.state) {
                NodeState.OWNED -> Text(
                    text = stringResource(R.string.node_owned),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = Teal
                )
                NodeState.AVAILABLE -> Text(
                    text = stringResource(R.string.node_cost, node.cost),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = if (affordable) Saffron else TextMuted
                )
                NodeState.LOCKED -> Text(
                    text = stringResource(R.string.node_locked),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }
        }
    }
}
