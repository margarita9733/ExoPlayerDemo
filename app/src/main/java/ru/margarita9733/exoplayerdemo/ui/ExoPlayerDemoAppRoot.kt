package ru.margarita9733.exoplayerdemo.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.margarita9733.exoplayerdemo.R
import ru.margarita9733.exoplayerdemo.navigation.AppNavHost

@Composable
fun ExoPlayerDemoAppRoot(
    modifier: Modifier = Modifier,
    appState: AppState,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = { TopBar() },
        containerColor = Color.Transparent
    ) { padding ->
        AppNavHost(
            appState = appState,
            paddingValues = padding,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { AppBarTitle() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White.copy(alpha = 0.5f),
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = Color.Transparent,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
}

@Composable
fun AppBarTitle() {
    Text(
        text = stringResource(R.string.home_toolbar_title),
        style = MaterialTheme.typography.titleLarge,
        fontFamily = FontFamily(Font(R.font.gravitas_one_regular)),
        fontWeight = FontWeight(900),
    )
}