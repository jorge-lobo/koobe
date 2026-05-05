package com.jorgelobo.koobe.domain.usecase.category

sealed class CategoryValidationException : Exception() {
    class EmptyName : CategoryValidationException()
    class DuplicateName : CategoryValidationException()
    class NameTooShort : CategoryValidationException()
    class InvalidFirstCharacter : CategoryValidationException()
}