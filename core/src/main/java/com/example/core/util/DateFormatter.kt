package com.example.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateFormatter {
    private val isoParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    private val displayFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun format(isoString: String?): String {
        if (isoString.isNullOrBlank()) return "Tarih bilgisi yok"
        return try {
            val date = isoParser.parse(isoString)
            if (date != null) {
                displayFormatter.format(date)
            } else {
                isoString
            }
        } catch (e: Exception) {
            // Try secondary format if needed
            try {
                val secondaryParser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val date = secondaryParser.parse(isoString)
                if (date != null) displayFormatter.format(date) else isoString
            } catch (e2: Exception) {
                isoString
            }
        }
    }
}
