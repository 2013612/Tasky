package com.example.tasky.model.agenda

import io.ktor.resources.Resource

@Resource("/event")
class EventPath

typealias UpdateEventBody = Event
