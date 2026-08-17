package com.example.parking.external.publicdata

import com.example.parking.config.PublicDataApiProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class PublicParkingApiClient(
    private val webClient: WebClient,
    private val properties: PublicDataApiProperties
) {

    fun fetchParkingLots(
        pageNo: Int = 1,
        numOfRows: Int = 10
    ): String {

        println("=== 설정값 전달 확인 ===")
        println("properties.baseUrl = ${properties.baseUrl}")
        println("properties.serviceKey 존재 = ${properties.serviceKey.isNotBlank()}")
        println("properties.serviceKey 길이 = ${properties.serviceKey.length}")

        return webClient.get()
//            .uri { builder ->
//                builder
//                    .scheme("https")
//                    .host("api.data.go.kr")
//                    .path("/openapi/tn_pubr_prkplce_info_api")
//                    .query("serviceKey=${properties.serviceKey}&pageNo=$pageNo&numOfRows=$numOfRows&type=json")
//                    .build()
//            }
            .uri { builder ->
                val uri = builder
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

                println("URI rawQuery = ${uri.rawQuery?.replace(properties.serviceKey, "[KEY]")}")

                uri
            }
            .exchangeToMono { response ->
                response.bodyToMono(String::class.java)
                    .map { body ->
                        "HTTP Status: ${response.statusCode()}\n$body"
                    }
            }
            .block()
            ?: throw IllegalStateException("공공데이터 API 응답이 없습니다.")
    }
}