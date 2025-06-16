package com.joao.otavio.design_system.missingPermissions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joao.otavio.core.util.NavigationEvent
import com.joao.otavio.core.util.PermissionUtils
import com.joao.otavio.design_system.buttons.WebTrackerButton
import com.joao.otavio.design_system.design.themes.WebTrackerTheme
import com.joao.otavio.design_system.dimensions.LocalDimensions
import com.joao.otavio.design_system.dimensions.LocalPaddings
import com.joao.otavio.design_system.headers.LightHeader
import com.joao.otavio.design_system.permissions.rememberPermissionHandler
import com.joao.otavio.design_system.scaffold.WebTrackerScaffold
import com.joao.otavio.design_system.snackbar.WebTrackerSnackBar
import com.joao.otavio.webtracker.common.desygn.system.R

@Composable
fun WebTrackerMissingPermissionsScreen(
    modifier: Modifier = Modifier,
    navigation: (NavigationEvent) -> Unit,
) {
    val paddings = LocalPaddings.current
    val dimensions = LocalDimensions.current
    val context = LocalContext.current
    val activity = context as? Activity
    var showSnackBar by remember { mutableStateOf(false) }
    val doWithGrantedPermissions = rememberPermissionHandler(
        context = context,
    )

    BackHandler(enabled = true) {
        doWithGrantedPermissions(
            {
                navigation.invoke(NavigationEvent.NavigateUp)
            },
            {
                showSnackBar = true
            }
        )
    }

    val areAllPermissionsGranted = remember(context) {
        PermissionUtils.checkAllPermissionsGranted(context)
    }

    val openDeviceSettings = {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(0)
            data = Uri.fromParts("package", context.packageName, null)
        }
        activity?.startActivityForResult(intent, APP_SETTINGS_REQUEST_CODE)
    }

    LaunchedEffect(Unit) {
        if (!areAllPermissionsGranted) {
            activity?.requestPermissions(
                PermissionUtils.appGeneralPermissions(),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    WebTrackerScaffold(
        modifier = modifier,
        topBar = {
            LightHeader(
                title = stringResource(R.string.missingPermissionsScreen_screen_title),
                onClickLeft = {
                    doWithGrantedPermissions(
                        {
                            navigation.invoke(NavigationEvent.NavigateUp)
                        },
                        {
                            showSnackBar = true
                        }
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(paddings.medium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_img_no_signal_gps),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = dimensions.ultra, height = dimensions.xxMega)
                            .padding(bottom = paddings.xSmall)
                    )

                    Text(
                        text = stringResource(R.string.missingPermissionsScreen_title),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily(Font(R.font.rubik_medium)),
                        textAlign = TextAlign.Center,
                        color = WebTrackerTheme.primaryText
                    )

                    Text(
                        text = stringResource(R.string.missingPermissionsScreen_explanationText),
                        modifier = Modifier.padding(top = paddings.medium),
                        style = MaterialTheme.typography.bodyLarge,
                        fontFamily = FontFamily(Font(R.font.rubik)),
                        textAlign = TextAlign.Center
                    )
                }
            }

            WebTrackerSnackBar(
                visible = showSnackBar,
                title = stringResource(R.string.missingPermissionsScreen_snackBar_title),
                subtitle = stringResource(R.string.missingPermissionsScreen_snackBar_subtitle),
                iconId = R.drawable.ic_close,
                backgroundColor = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.onError,
                iconColor = MaterialTheme.colorScheme.onError,
                duration = SnackbarDuration.Short.ordinal,
                onDismiss = { showSnackBar = false }
            )

            WebTrackerButton(
                text = stringResource(R.string.missingPermissionsScreen_openSettings),
                onClick = { openDeviceSettings.invoke() },
                modifier = Modifier.padding(horizontal = paddings.xSmall)
            )
        }
    }
}

@Preview
@Composable
fun WebTrackerMissingPermissionsScreenPreview() {
    WebTrackerMissingPermissionsScreen(
        navigation = {}
    )
}

private const val PERMISSIONS_REQUEST_CODE = 621
private const val APP_SETTINGS_REQUEST_CODE = 623
