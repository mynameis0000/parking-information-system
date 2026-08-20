package com.example.parking.controller

import com.example.parking.external.publicdata.PublicParkingApiClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.parking.external.publicdata.dto.PublicParkingResponse

@RestController
@RequestMapping("/api/test")
class PublicDataTestController(
    private val client: PublicParkingApiClient
) {

    @GetMapping("/public-data")
    fun test(): PublicParkingResponse {
        return client.fetchParkingLots()
    }
}