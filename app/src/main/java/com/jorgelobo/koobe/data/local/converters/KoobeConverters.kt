package com.jorgelobo.koobe.data.local.converters

import androidx.room.TypeConverter
import com.jorgelobo.koobe.domain.model.constants.enums.CurrencyType
import com.jorgelobo.koobe.domain.model.constants.enums.PaymentMethodType
import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType
import com.jorgelobo.koobe.domain.model.constants.enums.TransactionType

object KoobeConverters {

    // Converters for the TransactionType enum
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionType = TransactionType.valueOf(value)

    // Converters for the PaymentMethodType enum
    @TypeConverter
    fun fromPaymentMethodType(type: PaymentMethodType?): String? = type?.name

    @TypeConverter
    fun toPaymentMethodType(value: String?): PaymentMethodType? =
        value?.let { PaymentMethodType.valueOf(it) }

    // Converters for the CurrencyType enum
    @TypeConverter
    fun fromCurrencyType(type: CurrencyType): String = type.name

    @TypeConverter
    fun toCurrencyType(value: String): CurrencyType = CurrencyType.valueOf(value)

    // Converters for the PeriodType enum
    @TypeConverter
    fun fromPeriodType(type: PeriodType?): String? = type?.name

    @TypeConverter
    fun toPeriodType(value: String?): PeriodType? = value?.let { PeriodType.valueOf(it) }
}