package com.example.tasky.model.agenda

import io.ktor.resources.Resource

@Resource("/task")
class TaskPath

typealias UpdateTaskBody = Task

typealias CreateTaskBody = Task
