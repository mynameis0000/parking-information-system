package com.example.battery.repository

import com.example.battery.domain.Battery
import com.example.battery.domain.BatteryStatus
import com.example.battery.domain.Rental
import com.example.battery.domain.Station
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface StationRepository : JpaRepository<Station, Long> {

    @Query(
        value = """
            SELECT s.*, 
                   (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) 
                   * cos(radians(s.longitude) - radians(:lng)) 
                   + sin(radians(:lat)) * sin(radians(s.latitude)))) AS distance
            FROM station s
            WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(s.latitude)) 
                   * cos(radians(s.longitude) - radians(:lng)) 
                   + sin(radians(:lat)) * sin(radians(s.latitude)))) <= :radius
            ORDER BY distance ASC
        """, 
        nativeQuery = true
    )
    fun findNearbyStations(
        @Param("lat") lat: Double,
        @Param("lng") lng: Double,
        @Param("radius") radius: Double = 3.0
    ): List<Station>
}

interface BatteryRepository : JpaRepository<Battery, Long> {
    fun findFirstByStationIdAndStatus(stationId: Long, status: BatteryStatus): Battery?
}

interface RentalRepository : JpaRepository<Rental, Long>