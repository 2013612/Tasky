package com.example.tasky.model.agenda

import io.ktor.resources.Resource

@Resource("/reminder")
class ReminderPath

typealias UpdateReminderBody = Reminder

typealias CreateReminderBody = Reminder
