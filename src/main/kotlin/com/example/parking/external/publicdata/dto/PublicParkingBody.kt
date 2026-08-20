package com.example.parking.external.publicdata.dto

data class PublicParkingBody(
    val items: PublicParkingItems,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)