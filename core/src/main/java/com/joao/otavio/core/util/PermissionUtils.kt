package com.joao.otavio.core.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtils {
    fun appGeneralPermissions(): Array<String> {
        val permissionsList = mutableListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsList.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return permissionsList.toTypedArray()
    }

    fun appLocationPermissions(): Array<String> {
        val permissionsList: Array<String> = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        return permissionsList
    }

    fun appBackgroundLocationPermissions(): Array<String> {
        val permissionsList: MutableList<String> = mutableListOf()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        return permissionsList.toTypedArray()
    }

    fun checkLocationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkAppGeneralPermissionsGranted(context: Context): Boolean {
        return appGeneralPermissions().all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun checkAllPermissionsGranted(context: Context): Boolean {
        val allPermissions =
            appGeneralPermissions() + appLocationPermissions() + appBackgroundLocationPermissions()
        return allPermissions.all { permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun Map<String, Boolean>.areAllPermissionsGranted() = entries.map { permissionEntry ->
        permissionEntry.value
    }.all { granted ->
        granted
    }
}
