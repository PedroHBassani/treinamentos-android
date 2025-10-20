package com.loc.newsapp.utils

import java.text.SimpleDateFormat
import java.util.Locale.getDefault

object Formatter {

    fun formatDate(dateString: String): String {
        val inputFormat =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", getDefault())
        val outputFormat =
            SimpleDateFormat("MMMM dd, yyyy", getDefault())
        return try {
            val date = inputFormat.parse(dateString)
            if (date != null) {
                outputFormat.format(date)
            } else {
                dateString
            }
        } catch (e: Exception) {
            dateString
        }
    }
}
