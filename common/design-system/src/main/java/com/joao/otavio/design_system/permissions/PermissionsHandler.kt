package com.joao.otavio.design_system.permissions

import android.content.Context
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.joao.otavio.core.navigation.WebTrackerScreens
import com.joao.otavio.core.navigation.NavigationEvent
import com.joao.otavio.core.util.PermissionUtils
import com.joao.otavio.core.util.PermissionUtils.areAllPermissionsGranted
import com.joao.otavio.utils.click.ClickUtils.doIfCanClick


@Composable
fun PermissionsHandler(
    context: Context,
    navigation: (NavigationEvent.Navigate) -> Unit,
) {
    val permissionState = rememberPermissionState()

    SetupPermissionLaunchers(
        permissionState = permissionState,
        navigation = navigation
    )

    HandleInitialPermissionCheck(
        context = context,
        permissionState = permissionState
    )

    if (permissionState.showLocationDialog) {
        ShowLocationPermissionDialog(
            permissionState = permissionState,
            navigation = navigation
        )
    }
}

@Composable
private fun rememberPermissionState(): PermissionState {
    return remember {
        PermissionState(
            isFirstLaunch = true,
            showLocationDialog = false
        )
    }
}

@Composable
private fun SetupPermissionLaunchers(
    permissionState: PermissionState,
    navigation: (NavigationEvent.Navigate) -> Unit
) {
    val requestGeneralPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.areAllPermissionsGranted()) {
            permissionState.showLocationDialog = true
        } else {
            onMissingPermission(navigation)
        }
    }

    val requestBackgroundLocationPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (!permissions.areAllPermissionsGranted()) {
            onMissingPermission(navigation)
        }
    }

    val requestLocationPermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        handleLocationPermissionResult(
            permissions = permissions,
            requestBackgroundLocationPermissions = requestBackgroundLocationPermissions,
            navigation = navigation
        )
    }

    permissionState.apply {
        this.requestGeneralPermissions = requestGeneralPermissions
        this.requestLocationPermissions = requestLocationPermissions
    }
}

@Composable
private fun HandleInitialPermissionCheck(
    context: Context,
    permissionState: PermissionState
) {
    LaunchedEffect(permissionState.isFirstLaunch) {
        if (permissionState.isFirstLaunch) {
            checkInitialPermissions(context, permissionState)
            permissionState.isFirstLaunch = false
        }
    }
}

@Composable
private fun ShowLocationPermissionDialog(
    permissionState: PermissionState,
    navigation: (NavigationEvent.Navigate) -> Unit
) {
    Dialog(
        onDismissRequest = {
            permissionState.showLocationDialog = false
            onMissingPermission(navigation)
        }
    ) {
        LocationPermissionDialog(
            onConfirm = {
                permissionState.showLocationDialog = false
                permissionState.requestLocationPermissions?.launch(
                    PermissionUtils.appLocationPermissions()
                )
            },
            onCancel = {
                permissionState.showLocationDialog = false
                onMissingPermission(navigation)
            }
        )
    }
}

private class PermissionState(
    isFirstLaunch: Boolean,
    showLocationDialog: Boolean
) {
    var isFirstLaunch by mutableStateOf(isFirstLaunch)
    var showLocationDialog by mutableStateOf(showLocationDialog)
    var requestGeneralPermissions: ActivityResultLauncher<Array<String>>? = null
    var requestLocationPermissions: ActivityResultLauncher<Array<String>>? = null
}

private fun handleLocationPermissionResult(
    permissions: Map<String, Boolean>,
    requestBackgroundLocationPermissions: ActivityResultLauncher<Array<String>>,
    navigation: (NavigationEvent.Navigate) -> Unit
) {
    if (permissions.areAllPermissionsGranted()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestBackgroundLocationPermissions.launch(
                PermissionUtils.appBackgroundLocationPermissions()
            )
        }
    } else {
        onMissingPermission(navigation)
    }
}

private fun checkInitialPermissions(
    context: Context,
    permissionState: PermissionState
) {
    if (!PermissionUtils.checkAppGeneralPermissionsGranted(context)) {
        permissionState.requestGeneralPermissions?.launch(
            PermissionUtils.appGeneralPermissions()
        )
    } else if (!PermissionUtils.checkLocationPermissionGranted(context)) {
        permissionState.showLocationDialog = true
    }
}

private fun onMissingPermission(onNavigate: (NavigationEvent.Navigate) -> Unit) {
    onNavigate.invoke(
        WebTrackerScreens.MissingPermissions.navigateKeepingInStack()
    )
}

@Composable
fun rememberPermissionHandler(
    context: Context,
): (action: () -> Unit, onDenied: () -> Unit) -> Unit {
    return remember(context) {
        { action, onDenied ->
            doIfCanClick {
                if (PermissionUtils.checkAllPermissionsGranted(context)) {
                    action()
                } else {
                    onDenied()
                }
            }
        }
    }
}
