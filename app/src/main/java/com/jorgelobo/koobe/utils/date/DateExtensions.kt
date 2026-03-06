package com.jorgelobo.koobe.utils.date

import java.util.Calendar
import java.util.Date

fun Date.get(field: Int): Int =
    DateUtils.withCalendar(this) { get(field) }

fun Date.modify(block: Calendar.() -> Unit): Date =
    DateUtils.withCalendar(this) {
        block()
        time
    }

fun Date.year(): Int =
    get(Calendar.YEAR)

fun Date.month(): Int =
    get(Calendar.MONTH)

fun Date.week(): Int =
    get(Calendar.WEEK_OF_YEAR)

fun Date.day(): Int =
    get(Calendar.DAY_OF_MONTH)

fun Date.dayOfYear(): Int =
    get(Calendar.DAY_OF_YEAR)