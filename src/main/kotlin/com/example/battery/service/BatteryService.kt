package com.example.battery.service

import com.example.battery.domain.BatteryStatus
import com.example.battery.domain.Rental
import com.example.battery.domain.Station
import com.example.battery.repository.BatteryRepository
import com.example.battery.repository.RentalRepository
import com.example.battery.repository.StationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BatteryService(
    private val stationRepository: StationRepository,
    private val batteryRepository: BatteryRepository,
    private val rentalRepository: RentalRepository
) {

    @Transactional(readOnly = true)
    fun getNearbyStations(lat: Double, lng: Double, radiusKm: Double): List<Station> {
        return stationRepository.findNearbyStations(lat, lng, radiusKm)
    }

    @Transactional
    fun rentBattery(userId: Long, stationId: Long): Rental {
        val station = stationRepository.findById(stationId)
            .orElseThrow { IllegalArgumentException("존재하지 않는 거치대입니다.") }

        val battery = batteryRepository.findFirstByStationIdAndStatus(stationId, BatteryStatus.AVAILABLE)
            ?: throw IllegalStateException("현재 해당 거치대에 대여 가능한 배터리가 없습니다.")

        // 1. 거치대의 잔여 배터리 수 감소
        station.decreaseAvailableCount()

        // 2. 배터리 상태 변경 (대여 중)
        battery.rent()

        // 3. 대여 이력 저장
        val rental = Rental(
            userId = userId,
            battery = battery,
            rentStation = station
        )

        return rentalRepository.save(rental)
    }
}