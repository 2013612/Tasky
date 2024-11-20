package com.example.tasky.android.alarm.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.tasky.agenda.domain.model.AgendaType

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        val id = intent?.getStringExtra(AlarmIntentKey.ID.name) ?: return
        val title = intent.getStringExtra(AlarmIntentKey.TITLE.name) ?: return
        val type =
            if (Build.VERSION.SDK_INT >= 33) {
                intent.getSerializableExtra(AlarmIntentKey.TYPE.name, AgendaType::class.java)
            } else {
                intent.getSerializableExtra(AlarmIntentKey.TYPE.name) ?: return
            }

        println("onReceive $id $title $type")
    }
}
