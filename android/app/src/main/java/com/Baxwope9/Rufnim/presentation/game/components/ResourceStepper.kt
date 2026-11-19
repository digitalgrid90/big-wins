package com.Baxwope9.Rufnim.presentation.game.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextMuted
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.presentation.common.Format

@Composable
fun ResourceStepper(
    label: String,
    assigned: Int,
    demand: Int,
    canIncrease: Boolean,
    canDecrease: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.width(56.dp)
        )
        Box(modifier = Modifier.width(64.dp), contentAlignment = Alignment.Center) {
            Text(
                text = Format.ratio(assigned, demand),
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = if (assigned >= demand) Teal else Saffron
            )
        }
        IconButton(
            onClick = onDecrease,
            enabled = canDecrease,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_minus),
                contentDescription = stringResource(R.string.cd_decrease),
                tint = if (canDecrease) Saffron else TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
        IconButton(
            onClick = onIncrease,
            enabled = canIncrease,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_plus),
                contentDescription = stringResource(R.string.cd_increase),
                tint = if (canIncrease) Saffron else TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
