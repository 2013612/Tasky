package com.example.tasky.agenda.data.model

import com.example.tasky.agenda.domain.model.Task
import io.ktor.resources.Resource

@Resource("/task")
class TaskPath

typealias UpdateTaskBody = Task

typealias CreateTaskBody = Task
