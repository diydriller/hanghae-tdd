package io.hhplus.tdd.service

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.point.PointHistory
import org.springframework.stereotype.Service

@Service
class PointHistoryManagementService(
    private val pointHistoryTable: PointHistoryTable
) {
    fun getUserPointHistory(userId: Long): List<PointHistory> {
        return pointHistoryTable.selectAllByUserId(userId)
    }
}