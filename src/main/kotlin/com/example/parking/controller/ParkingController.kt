package com.example.parking.controller

import com.example.parking.dto.ParkingResponse
import com.example.parking.service.ParkingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/parking")
class ParkingController(
    private val parkingService: ParkingService
) {

    @GetMapping("/search")
    fun searchParking(
        @RequestParam query: String,
        @RequestParam(required = false) x: Double?,
        @RequestParam(required = false) y: Double?,
        @RequestParam(required = false, defaultValue = "1000") radius: Int?
    ): ResponseEntity<List<ParkingResponse>> {
        val results = parkingService.searchParkingLots(query, x, y, radius)
        return ResponseEntity.ok(results)
    }
}
