package com.example.parking.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "public-data.api")
data class PublicDataApiProperties(
    val baseUrl: String,
    val serviceKey: String
)