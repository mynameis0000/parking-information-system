package com.example.battery.service

import com.example.battery.repository.StationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

data class ReturnResult(
    val stationName: String,
    val totalMinutes: Long,
    val totalPrice: Int
)

@Service
class ReturnService(
    private val stationRepository: StationRepository
) {

    @Transactional
    fun returnBattery(stationId: Long, startTime: LocalDateTime): ReturnResult {
        val station = stationRepository.findById(stationId)
            .orElseThrow { IllegalArgumentException("거치대를 찾을 수 없습니다.") }

        // 1. 거치대 반납 가능 수량 증가 (최대 슬롯 수를 넘지 않도록 처리)
        if (station.availableCount >= station.totalSlots) {
            throw IllegalStateException("거치대에 빈 슬롯이 없습니다.")
        }
        station.availableCount += 1
        stationRepository.save(station)

        // 2. 이용 시간 및 요금 계산
        val endTime = LocalDateTime.now()
        val durationMinutes = Duration.between(startTime, endTime).toMinutes().coerceAtLeast(1) // 최소 1분 계산

        // 요금 정책: 기본 요금 1,000원 (10분 기본) + 10분 초과 시 분당 100원
        val basePrice = 1000
        val baseMinutes = 10L
        val extraPricePerMinute = 100

        val totalPrice = if (durationMinutes <= baseMinutes) {
            basePrice
        } else {
            basePrice + ((durationMinutes - baseMinutes) * extraPricePerMinute).toInt()
        }

        return ReturnResult(
            stationName = station.name,
            totalMinutes = durationMinutes,
            totalPrice = totalPrice
        )
    }
}