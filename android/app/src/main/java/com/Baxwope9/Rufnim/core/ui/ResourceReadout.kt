package com.Baxwope9.Rufnim.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.TextMuted

@Composable
fun ResourceReadout(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
    accent: Color = Saffron
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = if (value > 0) accent else TextMuted
        )
    }
}
