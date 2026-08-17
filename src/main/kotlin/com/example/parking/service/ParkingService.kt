package com.example.parking.service

import com.example.parking.dto.KakaoPlaceDocument
import com.example.parking.dto.KakaoSearchResponse
import com.example.parking.dto.ParkingResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class ParkingService(
    private val restTemplate: RestTemplate,
    @Value("\${kakao.api.key}") private val apiKey: String
) {

    private val kakaoApiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json"

    fun searchParkingLots(query: String, x: Double?, y: Double?, radius: Int?): List<ParkingResponse> {
        val searchQuery = if (query.contains("주차장")) query else "$query 주차장"

        // .encode()를 반드시 추가해야 한글 검색어가 깨지지 않고 400 에러가 나지 않습니다.
        val builder = UriComponentsBuilder.fromUriString(kakaoApiUrl)
            .queryParam("query", searchQuery)
            .queryParam("category_group_code", "PK6")

        if (x != null && y != null) {
            builder.queryParam("x", x)
            builder.queryParam("y", y)
            if (radius != null) {
                builder.queryParam("radius", radius)
            }
        }

        // build().encode().toUri() 로 처리
        val targetUri = builder.build().encode().toUri()

        val headers = HttpHeaders().apply {
            set("Authorization", "KakaoAK $apiKey")
        }

        val entity = HttpEntity<Unit>(headers)

        val response: ResponseEntity<KakaoSearchResponse> = restTemplate.exchange(
            targetUri,
            HttpMethod.GET,
            entity,
            KakaoSearchResponse::class.java
        )

        val documents: List<KakaoPlaceDocument> = response.body?.documents ?: emptyList()

        return documents.map { doc ->
            ParkingResponse(
                id = doc.id,
                name = doc.placeName,
                address = doc.addressName,
                roadAddress = doc.roadAddressName,
                phone = doc.phone,
                longitude = doc.longitude.toDoubleOrNull() ?: 0.0,
                latitude = doc.latitude.toDoubleOrNull() ?: 0.0,
                distance = doc.distance,
                mapUrl = doc.placeUrl
            )
        }
    }
}