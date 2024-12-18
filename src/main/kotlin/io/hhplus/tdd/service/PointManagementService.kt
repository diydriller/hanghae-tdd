package io.hhplus.tdd.service

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.exception.ExceptionStatus
import io.hhplus.tdd.lock.SynchronizeWithKey
import io.hhplus.tdd.point.UserPoint
import org.springframework.stereotype.Service

@Service
class PointManagementService(
    private val userPointTable: UserPointTable,

    ) {
    fun getUserPoint(userId: Long): UserPoint {
        return userPointTable.selectById(userId)
    }

    @SynchronizeWithKey(key = "user-#{#userId}-transaction")
    fun chargeUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        return userPointTable.insertOrUpdate(userPoint.id, userPoint.point + amount)
    }

    @SynchronizeWithKey(key = "user-#{#userId}-transaction")
    fun useUserPoint(userId: Long, amount: Long): UserPoint {
        val userPoint = userPointTable.selectById(userId)
        if (userPoint.point < amount) {
            throw RuntimeException(ExceptionStatus.NOT_ENOUGH_BALANCE.value)
        }
        return userPointTable.insertOrUpdate(userPoint.id, userPoint.point - amount)
    }
}