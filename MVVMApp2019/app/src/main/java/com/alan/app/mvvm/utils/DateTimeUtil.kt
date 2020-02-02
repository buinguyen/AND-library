package com.alan.app.mvvm.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtil {

    const val DATE_TEXT_1 = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    const val DATE_TEXT_2 = "dd/MM/yyyy"

    fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.US)
        return formatter.format(date)
    }

    fun toDate(time: String?, format: String): Date? {
        time ?: return null
        return try {
            val formatter = SimpleDateFormat(format, Locale.US)
            formatter.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }
}