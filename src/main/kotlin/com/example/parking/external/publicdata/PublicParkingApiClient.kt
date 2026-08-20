package com.example.parking.external.publicdata

import com.example.parking.config.PublicDataApiProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import com.example.parking.external.publicdata.dto.PublicParkingResponse

@Component
class PublicParkingApiClient(
    private val webClient: WebClient,
    private val properties: PublicDataApiProperties
) {

    fun fetchParkingLots(
        pageNo: Int = 1,
        numOfRows: Int = 10
    ): PublicParkingResponse {

        return webClient.get()
            .uri { builder ->
                builder
                    .scheme("https")
                    .host("api.data.go.kr")
                    .path("/openapi/tn_pubr_prkplce_info_api")
                    .query(
                        "serviceKey=${properties.serviceKey}" +
                                "&pageNo=$pageNo" +
                                "&numOfRows=$numOfRows" +
                                "&type=json"
                    )
                    .build()
            }
            .retrieve()
            .bodyToMono(PublicParkingResponse::class.java)
            .block()
            ?: throw IllegalStateException("공공데이터 API 응답이 없습니다.")
    }
}