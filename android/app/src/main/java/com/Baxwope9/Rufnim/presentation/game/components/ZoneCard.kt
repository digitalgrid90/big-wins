package com.Baxwope9.Rufnim.presentation.game.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.Hazard
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.SurfacePanel
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.presentation.common.Format
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.game.ZoneUi

@Composable
fun ZoneCard(
    zone: ZoneUi,
    selected: Boolean,
    width: Dp,
    height: Dp,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    val coverage by animateFloatAsState(
        targetValue = zone.coverage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 420),
        label = "coverage"
    )
    val coverageColor = when {
        coverage >= 0.999f -> Teal
        coverage >= 0.5f -> Saffron
        else -> Hazard
    }
    val selectedLabel = stringResource(R.string.cd_selected)
    val idleLabel = stringResource(R.string.cd_not_selected)
    Column(
        modifier = modifier
            .size(width = width, height = height)
            .clip(shape)
            .background(SurfacePanel.copy(alpha = 0.92f), shape)
            .border(1.dp, if (selected) Saffron else Hairline, shape)
            .clickable(onClick = onSelect)
            .semantics { stateDescription = if (selected) selectedLabel else idleLabel }
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(UiLabels.zoneName(zone.zone)),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                letterSpacing = 1.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            if (zone.fatigued) {
                Text(
                    text = stringResource(R.string.tag_fatigue),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 8.sp,
                    letterSpacing = 1.sp,
                    color = Hazard
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Image(
                painter = painterResource(UiLabels.zoneSprite(zone.zone)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(34.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                zone.demand.entries
                    .filter { it.value > 0 }
                    .forEach { entry ->
                        val given = zone.assigned[entry.key] ?: 0
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(UiLabels.resourceLabel(entry.key)),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                letterSpacing = 0.5.sp,
                                color = TextMuted,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = Format.ratio(given, entry.value),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                letterSpacing = 0.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (given >= entry.value) Teal else TextPrimary
                            )
                        }
                    }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Hairline)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(coverage.coerceAtLeast(0.02f))
                    .height(3.dp)
                    .background(coverageColor)
            )
        }
    }
}
