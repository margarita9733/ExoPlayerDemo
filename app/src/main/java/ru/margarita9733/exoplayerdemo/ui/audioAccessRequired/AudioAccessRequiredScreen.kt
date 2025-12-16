package ru.margarita9733.exoplayerdemo.ui.audioAccessRequired

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.margarita9733.exoplayerdemo.R
import ru.margarita9733.exoplayerdemo.ui.commonComponents.CommonBlockTextWithButton
import ru.margarita9733.exoplayerdemo.utils.getReadAudioPermissionTypeBySDK

@Composable
fun AudioAccessRequiredScreenRoot(
    onNavigateToHome: () -> Unit = {},
    onTogglePermission: (Boolean) -> Unit,
) {

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->

        Log.d("AudioAccessRequiredScreenRoot", "permissionResultFromLauncher: $isGranted")
        onTogglePermission(isGranted)
        if (isGranted) onNavigateToHome.invoke()
    }

    CommonBlockTextWithButton(
        infoText = stringResource(R.string.home_no_data_provided_placeholder),
        buttonText = stringResource(R.string.provide_tracks),
        onButtonClick = { launcher.launch(getReadAudioPermissionTypeBySDK()) },
    )

}