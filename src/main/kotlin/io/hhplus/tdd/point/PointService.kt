package io.hhplus.tdd.point

import io.hhplus.tdd.database.UserPointTable
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userPointTable: UserPointTable
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }
}