package com.jorgelobo.koobe.domain.model.budget

import com.jorgelobo.koobe.domain.model.constants.enums.PeriodType

data class Budget(
    val id: Int,
    val categoryId: Int,
    val subcategoryId: Int,
    val period: PeriodType,
    val repeat: Boolean,
    val limitAmount: Double,
    val spentAmount: Double,
    val projectedAmount: Double,
    val dailyAverage: Double
) {

    companion object {
        fun empty(): Budget = Budget(
            id = 0,
            categoryId = 0,
            subcategoryId = 0,
            period = PeriodType.MONTHLY,
            repeat = false,
            limitAmount = 0.0,
            spentAmount = 0.0,
            projectedAmount = 0.0,
            dailyAverage = 0.0
        )
    }
}