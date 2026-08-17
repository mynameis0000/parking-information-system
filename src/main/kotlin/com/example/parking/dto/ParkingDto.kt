package com.example.parking.dto

import com.fasterxml.jackson.annotation.JsonProperty

// 카카오 로컬 API 응답용 DTO
data class KakaoSearchResponse(
    @JsonProperty("documents")
    val documents: List<KakaoPlaceDocument> = emptyList(),
    @JsonProperty("meta")
    val meta: PlaceMeta? = null
)

data class PlaceMeta(
    @JsonProperty("total_count")
    val totalCount: Int,
    @JsonProperty("pageable_count")
    val pageableCount: Int,
    @JsonProperty("is_end")
    val isEnd: Boolean
)

data class KakaoPlaceDocument(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("place_name")
    val placeName: String,
    @JsonProperty("category_name")
    val categoryName: String,
    @JsonProperty("phone")
    val phone: String,
    @JsonProperty("address_name")
    val addressName: String,
    @JsonProperty("road_address_name")
    val roadAddressName: String,
    @JsonProperty("x")
    val longitude: String,
    @JsonProperty("y")
    val latitude: String,
    @JsonProperty("place_url")
    val placeUrl: String,
    @JsonProperty("distance")
    val distance: String? = null
)

// 클라이언트 전달용 응답 DTO
data class ParkingResponse(
    val id: String,
    val name: String,
    val address: String,
    val roadAddress: String,
    val phone: String,
    val longitude: Double,
    val latitude: Double,
    val distance: String?,
    val mapUrl: String
)