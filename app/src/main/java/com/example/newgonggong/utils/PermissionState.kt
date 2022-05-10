package com.example.newgonggong.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionState(
    val permission: String,
    val hasPermission: StateFlow<Boolean>,
    private val launcher: ActivityResultLauncher<String>
) {
    fun launchPermissionRequest() = launcher.launch(permission)
}

@ExperimentalCoroutinesApi
private class PermissionResultCall(
    key: String,
    private val activity: ComponentActivity,
    private val permission: String
) {

    private val hasPermission = MutableStateFlow<Boolean>(false)

    private var call = activity.activityResultRegistry.register(
        "LocationPermissions#($key)",
        ActivityResultContracts.RequestPermission()
    ) { result ->
        onPermissionResult(result)
    }

    fun initialCheck() {
        hasPermission.value = checkPermission()
    }

    fun unregister() {
        call.unregister()
    }

    fun checkSelfPermission(): PermissionState {
        return PermissionState(permission, hasPermission, call)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun onPermissionResult(result: Boolean) {
        hasPermission.value = result
    }
}

@ExperimentalComposeApi
@Composable
fun checkSelfPermissionState(activity: ComponentActivity, permission: String): PermissionState {
    val key = "1"
    val call = remember(activity, permission) {
        PermissionResultCall(key, activity, permission)
    }

    DisposableEffect(call) {
        call.initialCheck()
        onDispose {
            call.unregister()
        }
    }

    return call.checkSelfPermission()
}