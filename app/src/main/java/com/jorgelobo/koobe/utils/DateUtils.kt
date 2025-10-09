package com.jorgelobo.koobe.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {
    @SuppressLint("ConstantLocale")
    /*private val locale = Locale.getDefault()*/
    private val locale = Locale.ENGLISH

    val currentDate: Date
        get() = Calendar.getInstance().time

    fun formatDateLong(date: Date): String {
        val outputFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", locale)
        return outputFormat.format(date)
    }

    fun formatDateShort(date: Date): String {
        val outputFormat = SimpleDateFormat("MMMM yyyy", locale)
        return outputFormat.format(date)
    }
}