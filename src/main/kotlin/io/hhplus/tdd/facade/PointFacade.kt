package io.hhplus.tdd.facade

import io.hhplus.tdd.point.TransactionType
import io.hhplus.tdd.point.UserPoint
import io.hhplus.tdd.service.PointHistoryManagementService
import io.hhplus.tdd.service.PointManagementService
import org.springframework.stereotype.Component

@Component
class PointFacade(
    private val pointManagementService: PointManagementService,
    private val pointHistoryManagementService: PointHistoryManagementService
) {
    fun chargeUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = pointManagementService.chargeUserPoint(userId, amount)
        pointHistoryManagementService.recordUserPointHistory(userId, amount, TransactionType.CHARGE)
        return userPoint
    }

    fun useUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = pointManagementService.useUserPoint(userId, amount)
        pointHistoryManagementService.recordUserPointHistory(userId, amount, TransactionType.USE)
        return userPoint
    }
}