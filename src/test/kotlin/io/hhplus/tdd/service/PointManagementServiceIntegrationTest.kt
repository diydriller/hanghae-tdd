package io.hhplus.tdd.service

import io.hhplus.tdd.service.dto.ChargePointDto
import io.hhplus.tdd.service.dto.UsePointDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class PointManagementServiceIntegrationTest {
    @Autowired
    private lateinit var pointManagementService: PointManagementService

    @Test
    @DisplayName("synchronization integration test for charging user point")
    fun chargeUserPointSynchronizeTest() {
        val userId = 1L
        pointManagementService.chargeUserPoint(ChargePointDto(userId, 0L))

        runConcurrentTest(threadCount = 10, taskCount = 100) {
            pointManagementService.chargeUserPoint(ChargePointDto(userId, 10))
        }

        val finalPoint = pointManagementService.getUserPoint(userId)
        assertEquals(1000, finalPoint.point)
    }

    @Test
    @DisplayName("synchronization integration test for using user point")
    fun useUserPointSynchronizeTest() {
        val userId = 2L
        pointManagementService.chargeUserPoint(ChargePointDto(userId, 2000L))

        runConcurrentTest(threadCount = 10, taskCount = 100) {
            pointManagementService.useUserPoint(UsePointDto(userId, 10))
        }

        val finalPoint = pointManagementService.getUserPoint(userId)
        assertEquals(1000, finalPoint.point)
    }

    @Test
    @DisplayName("synchronization integration test for charging and using user point")
    fun chargeAndUseUserPointSynchronizeTest() {
        val userId = 3L
        pointManagementService.chargeUserPoint(ChargePointDto(userId, 2000L))

        runConcurrentTest(threadCount = 10, taskCount = 100) {
            pointManagementService.chargeUserPoint(ChargePointDto(userId, 10))
        }
        runConcurrentTest(threadCount = 10, taskCount = 100) {
            pointManagementService.useUserPoint(UsePointDto(userId, 20))
        }

        val finalPoint = pointManagementService.getUserPoint(userId)
        assertEquals(1000, finalPoint.point)
    }

    private fun runConcurrentTest(
        threadCount: Int, taskCount: Int, task: () -> Unit
    ) {
        val executor = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(taskCount)

        repeat(taskCount) {
            executor.submit {
                try {
                    task()
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executor.shutdown()
    }
}