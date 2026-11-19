package com.Baxwope9.Rufnim.presentation.howto

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Baxwope9.Rufnim.R
import com.Baxwope9.Rufnim.core.theme.NavyCanvas
import com.Baxwope9.Rufnim.core.theme.Saffron
import com.Baxwope9.Rufnim.core.theme.Teal
import com.Baxwope9.Rufnim.core.theme.TextPrimary
import com.Baxwope9.Rufnim.core.theme.TextSecondary
import com.Baxwope9.Rufnim.core.ui.FullRule
import com.Baxwope9.Rufnim.core.ui.ScreenBackground
import com.Baxwope9.Rufnim.core.ui.SecondaryButton
import com.Baxwope9.Rufnim.core.ui.SectionRule

@Composable
fun HowToPlayScreen(onBack: () -> Unit) {
    ScreenBackground(
        overlay = listOf(
            NavyCanvas.copy(alpha = 0.88f),
            NavyCanvas.copy(alpha = 0.96f)
        ),
        glow = Brush.radialGradient(
            colors = listOf(Saffron.copy(alpha = 0.08f), Color.Transparent),
            radius = 600f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.how_to_play),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            SectionRule(width = 52.dp)
            Spacer(modifier = Modifier.height(14.dp))
            HowToSection(
                index = R.string.step_one,
                title = R.string.howto_allocate_title,
                body = R.string.howto_allocate_body
            )
            HowToSection(
                index = R.string.step_two,
                title = R.string.howto_reserve_title,
                body = R.string.howto_reserve_body
            )
            HowToSection(
                index = R.string.step_three,
                title = R.string.howto_commit_title,
                body = R.string.howto_commit_body
            )
            HowToSection(
                index = R.string.step_four,
                title = R.string.howto_build_title,
                body = R.string.howto_build_body
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.howto_closing),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp,
                color = Teal
            )
            Spacer(modifier = Modifier.weight(1f))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun HowToSection(index: Int, title: Int, body: Int) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(index),
            style = MaterialTheme.typography.headlineMedium,
            color = Saffron,
            modifier = Modifier.size(width = 44.dp, height = 40.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(body),
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
    FullRule()
    Spacer(modifier = Modifier.height(10.dp))
}
