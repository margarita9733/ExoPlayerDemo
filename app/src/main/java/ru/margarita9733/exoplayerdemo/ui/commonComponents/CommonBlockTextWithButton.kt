package ru.margarita9733.exoplayerdemo.ui.commonComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.margarita9733.exoplayerdemo.R
import ru.margarita9733.exoplayerdemo.ui.theme.ExoPlayerDemoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonBlockTextWithButton(
    infoText: String = " Info text",
    buttonText: String = " Button text",
    toolbarHeight: Dp = TopAppBarDefaults.MediumAppBarCollapsedHeight,
    onButtonClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .padding(16.dp)
            .padding(
                WindowInsets.systemBars.add(
                    WindowInsets(top = toolbarHeight)
                ).asPaddingValues()
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = infoText,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            color = colorResource(R.color.gray),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(700)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onButtonClick
        )
        {
            Text(
                text = buttonText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight(500)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonBlockTextWithButtonPreview() {
    ExoPlayerDemoTheme {
        CommonBlockTextWithButton()
    }
}