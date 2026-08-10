package com.example.battery.controller

import com.example.battery.domain.Rental
import com.example.battery.domain.Station
import com.example.battery.service.BatteryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import com.example.battery.service.RentService // 1. import 추가
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

import com.example.battery.service.ReturnResult
import com.example.battery.service.ReturnService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*


@Tag(name = "Battery Sharing API", description = "배터리 공유 시스템 (조회, 대여, 반납)")
@RestController
@RequestMapping("/api/v1/batteries")
class BatteryController(
    private val batteryService: BatteryService,
    private val rentService: RentService,
    private val returnService: ReturnService
) {

    @Operation(summary = "반납 및 요금 계산", description = "거치대에 배터리를 반납하고 이용 시간을 기반으로 요금을 계산합니다.")
    @GetMapping("/return")
    fun returnBattery(
        @Parameter(description = "거치대 ID", example = "1")
        @RequestParam stationId: Long,
        @Parameter(description = "대여 시작 시각 차이(분 단위)", example = "25")
        @RequestParam minutesAgo: Long
    ): ReturnResult {
        val startTime = LocalDateTime.now().minusMinutes(minutesAgo)
        return returnService.returnBattery(stationId, startTime)
    }

    // 1. 주변 거치대 조회 API
    @GetMapping("/stations/nearby")
    fun getNearbyStations(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
        @RequestParam(defaultValue = "3.0") radiusKm: Double
    ): ResponseEntity<List<Station>> {
        val stations = batteryService.getNearbyStations(lat, lng, radiusKm)
        return ResponseEntity.ok(stations)
    }

    // 2. 보조배터리 대여 요청 API
    @PostMapping("/rent")
    fun rentBattery(
        @RequestParam userId: Long,
        @RequestParam stationId: Long
    ): ResponseEntity<RentalResponse> {
        val rental = batteryService.rentBattery(userId, stationId)
        return ResponseEntity.ok(
            RentalResponse(
                rentalId = rental.id,
                batterySerialNumber = rental.battery.serialNumber,
                rentStationName = rental.rentStation.name,
                rentedAt = rental.rentedAt.toString()
            )
        )
    }

    @GetMapping("/test-concurrency")
    fun testConcurrency(): String {
        val threadCount = 10
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)

        val stationId = 1L

        for (i in 1..threadCount) {
            val userId = i.toLong()
            executorService.submit {
                try {
                    rentService.rentBatteryWithLock(stationId, userId)
                } catch (e: Exception) {
                    println("대여 실패 (유저 $userId): ${e.message}")
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        return "동시성 테스트 완료! IntelliJ 콘솔 로그를 확인하세요."
    }


}


data class RentalResponse(
    val rentalId: Long,
    val batterySerialNumber: String,
    val rentStationName: String,
    val rentedAt: String
)