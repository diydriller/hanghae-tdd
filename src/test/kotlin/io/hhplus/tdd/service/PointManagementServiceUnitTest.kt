package io.hhplus.tdd.service

import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.exception.ExceptionStatus
import io.hhplus.tdd.point.UserPoint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PointManagementServiceUnitTest {
    @InjectMocks
    private lateinit var pointManagementService: PointManagementService

    @Mock
    private lateinit var userPointTable: UserPointTable

    @DisplayName("charging point method should increase user point")
    @Test
    fun chargingUserPointSuccessTest() {
        val userId = 1L
        val initialPoints = 100L

        val userPoint = UserPoint(userId, initialPoints, 10L)
        val chargeAmount = 50L
        val updatedUserPoint = UserPoint(userId, initialPoints + chargeAmount, 10L)

        // given
        `when`(userPointTable.selectById(userId)).thenReturn(userPoint)
        `when`(userPointTable.insertOrUpdate(userId, initialPoints + chargeAmount)).thenReturn(updatedUserPoint)

        // when
        val result = pointManagementService.chargeUserPoint(userId, chargeAmount)

        // then
        assertEquals(updatedUserPoint.point, result.point)
        verify(userPointTable, times(1)).selectById(userId)
        verify(userPointTable, times(1)).insertOrUpdate(userId, initialPoints + chargeAmount)
    }

    @DisplayName("using point method should decrease user point")
    @Test
    fun usingUserPointSuccessTest() {
        val userId = 1L
        val initialPoints = 100L

        val userPoint = UserPoint(userId, initialPoints, 10L)
        val useAmount = 50L
        val updatedUserPoint = UserPoint(userId, initialPoints - useAmount, 10L)

        // given
        `when`(userPointTable.selectById(userId)).thenReturn(userPoint)
        `when`(userPointTable.insertOrUpdate(userId, initialPoints - useAmount)).thenReturn(updatedUserPoint)

        // when
        val result = pointManagementService.useUserPoint(userId, useAmount)

        // then
        assertEquals(updatedUserPoint.point, result.point)
        verify(userPointTable, times(1)).selectById(userId)
        verify(userPointTable, times(1)).insertOrUpdate(userId, initialPoints - useAmount)
    }

    @DisplayName("using point more than balance should throw exception")
    @Test
    fun usingUserPointFailTest() {
        val userId = 1L
        val initialPoints = 100L

        val userPoint = UserPoint(userId, initialPoints, 10L)
        val useAmount = 200L

        // given
        `when`(userPointTable.selectById(userId)).thenReturn(userPoint)

        // when & then
        val exception = assertThrows(RuntimeException::class.java) {
            pointManagementService.useUserPoint(userId, useAmount)
        }
        assertEquals(ExceptionStatus.NOT_ENOUGH_BALANCE.value, exception.message)
        verify(userPointTable, times(1)).selectById(userId)
        verify(userPointTable, never()).insertOrUpdate(anyLong(), anyLong())
    }
}