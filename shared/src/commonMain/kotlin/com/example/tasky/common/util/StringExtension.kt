package com.example.tasky.common.util

fun String.getAvatarDisplayName(): String {
    val splitName = this.trim().split(" ")
    return when (splitName.size) {
        0 -> ""
        1 -> splitName[0].take(2)
        else -> splitName[0][0].toString() + splitName.last()[0]
    }
}
