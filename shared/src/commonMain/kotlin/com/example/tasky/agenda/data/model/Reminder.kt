package com.example.tasky.agenda.data.model

import com.example.tasky.agenda.domain.model.Reminder
import io.ktor.resources.Resource

@Resource("/reminder")
class ReminderPath

typealias UpdateReminderBody = Reminder

typealias CreateReminderBody = Reminder
