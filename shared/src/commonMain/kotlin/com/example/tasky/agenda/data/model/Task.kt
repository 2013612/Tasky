package com.example.tasky.agenda.data.model

import io.ktor.resources.Resource

@Resource("/task")
class TaskPath

typealias UpdateTaskBody = Task

typealias CreateTaskBody = Task
