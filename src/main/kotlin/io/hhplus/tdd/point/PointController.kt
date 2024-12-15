package io.hhplus.tdd.point

import io.hhplus.tdd.point.dto.ChargePointRequest
import io.hhplus.tdd.point.dto.UsePointRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    fun point(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
    ): UserPoint {
        return pointService.getUserPoint(id)
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    fun history(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
    ): List<PointHistory> {
        return pointService.getUserPointHistory(id)
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
        @RequestBody @Valid request: ChargePointRequest,
    ): UserPoint {
        return pointService.chargeUserPoint(id, request.amount)
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    fun use(
        @PathVariable @Min(value = 1, message = "id must be greater than 0") id: Long,
        @RequestBody @Valid request: UsePointRequest,
    ): UserPoint {
        return pointService.useUserPoint(id, request.amount)
    }
}