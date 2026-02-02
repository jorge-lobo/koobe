package com.jorgelobo.koobe.domain.amount

fun exceedsMax(value: String): Boolean {
    val number = value.toDoubleOrNull() ?: return false
    return number > MAX_AMOUNT
}

fun appendDigit(current: String, digit: Int): String {
    val next = when {
        current == "0" -> digit.toString()
        else -> current + digit
    }

    if (next.contains('.') && next.decimalCount() > MAX_DECIMALS) {
        return current
    }

    if (exceedsMax(next)) {
        return current
    }

    return next
}

fun appendDecimal(current: String): String {
    if (current.contains(".")) return current

    return if (current.isBlank()) "0." else "$current."
}

fun removeLast(current: String): String {
    if (current.isEmpty() || current == "0") return "0"

    val trimmed = current.dropLast(1)

    return when {
        trimmed.isEmpty() -> "0"
        trimmed == "." -> "0."
        else -> trimmed
    }
}