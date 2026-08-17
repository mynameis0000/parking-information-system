package com.example.battery.service

// 🔽 아래 2줄을 추가해 주세요!
import com.example.battery.repository.StationRepository
import com.example.battery.repository.BatteryRepository

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class RentService(
    private val redissonClient: RedissonClient,
    private val stationRepository: StationRepository,
    private val batteryRepository: BatteryRepository
) {

    fun rentBatteryWithLock(stationId: Long, userId: Long) {
        val lockKey = "lock:station:$stationId"
        val lock = redissonClient.getLock(lockKey)

        try {
            // 락 획득 시도 (최대 5초 대기, 락 획득 후 3초 뒤 자동 해제)
            val available = lock.tryLock(5, 3, TimeUnit.SECONDS)
            if (!available) {
                throw IllegalStateException("현재 대여 요청이 많아 처리할 수 없습니다. 잠시 후 다시 시도해주세요.")
            }

            // 락 획득 성공 후 실제 대여 로직 수행
            processRent(stationId, userId)

        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw RuntimeException("락 획득 중 인터럽트 발생", e)
        } finally {
            // 현재 쓰레드가 락을 점유하고 있다면 해제
            if (lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }

    @Transactional
    fun processRent(stationId: Long, userId: Long) {
        val station = stationRepository.findById(stationId)
            .orElseThrow { IllegalArgumentException("거치대를 찾을 수 없습니다.") }

        if (station.availableCount <= 0) {
            throw IllegalStateException("대여 가능한 배터리가 없습니다.")
        }

        // 수량 차감
        station.availableCount -= 1
        stationRepository.save(station)
    }
}