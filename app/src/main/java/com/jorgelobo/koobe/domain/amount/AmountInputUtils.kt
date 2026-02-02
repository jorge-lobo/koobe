package com.jorgelobo.koobe.domain.amount

fun String.decimalCount(): Int = substringAfter('.', "").length