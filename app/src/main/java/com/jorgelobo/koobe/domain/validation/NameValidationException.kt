package com.jorgelobo.koobe.domain.validation

sealed class NameValidationException() : Exception() {
    class Empty : NameValidationException()
    class TooShort : NameValidationException()
    class InvalidFirstCharacter : NameValidationException()
    class Duplicate : NameValidationException()
}