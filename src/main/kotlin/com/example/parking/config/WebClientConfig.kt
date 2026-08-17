package com.example.parking.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
class WebClientConfig {

    @Bean
    fun webClient(): WebClient {

        val factory = DefaultUriBuilderFactory()
        factory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE

        return WebClient.builder()
            .uriBuilderFactory(factory)
            .build()
    }
}