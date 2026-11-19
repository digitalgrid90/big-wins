package com.Baxwope9.Rufnim.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.theme.Hairline
import com.Baxwope9.Rufnim.core.theme.Hazard
import com.Baxwope9.Rufnim.core.theme.SurfaceElevated
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.core.ui.HairlinePanel
import com.Baxwope9.Rufnim.core.ui.SectionRule
import com.Baxwope9.Rufnim.domain.model.IncidentTone
import com.Baxwope9.Rufnim.presentation.common.UiLabels
import com.Baxwope9.Rufnim.presentation.game.IncidentUi

@Composable
fun IncidentDialog(
    incident: IncidentUi,
    onChoose: (Int) -> Unit
) {
    val accent = if (incident.card.tone == IncidentTone.HAZARD) Hazard else Teal
    val fallback = incident.options.firstOrNull { it.resource == null }
        ?: incident.options.firstOrNull { it.affordable }
    Dialog(
        onDismissRequest = { fallback?.let { onChoose(it.index) } },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        HairlinePanel(
            fill = SurfaceElevated.copy(alpha = 0.97f),
            border = accent,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = stringResource(UiLabels.incidentTitle(incident.card.id)),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            SectionRule(width = 48.dp, color = accent)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(UiLabels.incidentBody(incident.card.id)),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
            incident.options.forEach { option ->
                val label = if (option.resource == null) {
                    stringResource(R.string.branch_free)
                } else {
                    stringResource(
                        R.string.branch_cost,
                        option.cost,
                        stringResource(UiLabels.resourceLabel(option.resource))
                    )
                }
                OutlinedButton(
                    onClick = { onChoose(option.index) },
                    enabled = option.affordable,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, if (option.affordable) accent else Hairline),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = accent,
                        disabledContentColor = TextMuted
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center,
                        color = if (option.affordable) accent else TextMuted
                    )
                }
                if (!option.affordable) {
                    Text(
                        text = stringResource(R.string.branch_no_reserve),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                Text(
                    text = stringResource(R.string.incident_hint),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = TextMuted
                )
            }
        }
    }
}
