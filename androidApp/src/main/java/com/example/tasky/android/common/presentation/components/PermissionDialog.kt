package com.example.tasky.android.common.presentation.components

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.tasky.android.R
import com.example.tasky.android.common.presentation.utils.UiText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionDialog(
    permissionDialogProvider: PermissionDialogProvider,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val permissionState =
        rememberPermissionState(permissionDialogProvider.permission) { isGranted ->
            if (!isGranted) {
                showDialog = true
            }
        }

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    val isPermanentlyDeclined =
        !permissionState.status.shouldShowRationale

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    if (isPermanentlyDeclined) {
                        val intent =
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null),
                            )
                        context.startActivity(intent)
                    } else {
                        permissionState.launchPermissionRequest()
                        showDialog = false
                    }
                }) {
                    Text(
                        if (isPermanentlyDeclined) {
                            stringResource(R.string.setting)
                        } else {
                            stringResource(R.string.grant_permission)
                        },
                    )
                }
            },
            title = {
                Text(text = stringResource(R.string.permission_required))
            },
            text = {
                Text(
                    text =
                        permissionDialogProvider
                            .getDescription(
                                isPermanentlyDeclined = isPermanentlyDeclined,
                            ).asString(),
                )
            },
            modifier = modifier,
        )
    }
}

interface PermissionDialogProvider {
    val permission: String

    fun getDescription(isPermanentlyDeclined: Boolean): UiText
}

class NotificationPermissionDialogProvider : PermissionDialogProvider {
    override val permission: String
        get() = Manifest.permission.POST_NOTIFICATIONS

    override fun getDescription(isPermanentlyDeclined: Boolean): UiText =
        UiText.StringResource(
            if (isPermanentlyDeclined) {
                R.string.notification_permission_declined_desc
            } else {
                R.string.notification_permission_desc
            },
        )
}
