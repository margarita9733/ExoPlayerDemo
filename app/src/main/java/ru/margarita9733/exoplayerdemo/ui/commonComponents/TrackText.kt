package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ru.margarita9733.exoplayerdemo.R

@Composable
fun TrackText(
    modifier: Modifier = Modifier,
    title: String,
    artist: String,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = FontFamily.SansSerif,
            maxLines = 1,
            fontWeight = FontWeight(700),
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = artist,
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.gray),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight(700),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}