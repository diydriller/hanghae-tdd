package io.hhplus.tdd.service

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.lock.SynchronizeWithKey
import io.hhplus.tdd.point.UserPoint
import org.springframework.stereotype.Service

@Service
class PointManagementService(
    private val userPointTable: UserPointTable
) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }

    @SynchronizeWithKey(key = "user-#{#userId}-transaction")
    fun chargeUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        val updatedUserPoint = userPoint.charge(amount)
        return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }

    @SynchronizeWithKey(key = "user-#{#userId}-transaction")
    fun useUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        val updatedUserPoint = userPoint.use(amount)
        return userPointTable.insertOrUpdate(updatedUserPoint.id, updatedUserPoint.point)
    }
}