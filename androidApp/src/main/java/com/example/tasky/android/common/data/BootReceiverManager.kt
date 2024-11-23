package com.example.tasky.android.common.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.example.tasky.android.common.domain.IBootReceiverManager

class BootReceiverManager(
    private val context: Context,
) : IBootReceiverManager {
    private val receiver = ComponentName(context, BootReceiver::class.java)

    override fun start() {
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP,
        )
    }

    override fun stop() {
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP,
        )
    }
}
