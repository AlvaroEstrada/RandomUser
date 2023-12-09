package com.alvaroestrada.randomuser.extensions

import java.text.SimpleDateFormat
import java.util.Locale

fun String.toFormattedDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        val parsedDate = parser.parse(this)
        formatter.format(parsedDate)
    } catch (e: Exception) {
        "Invalid Date"
    }
}