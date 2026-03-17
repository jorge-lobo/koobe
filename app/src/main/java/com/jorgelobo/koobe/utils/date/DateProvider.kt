package com.jorgelobo.koobe.utils.date

import java.util.Calendar
import java.util.Date

object DateProvider {

    val now: Date
        get() = Calendar.getInstance().time
}