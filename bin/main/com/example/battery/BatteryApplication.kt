package com.example.battery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatteryApplication

fun main(args: Array<String>) {
    runApplication<BatteryApplication>(*args)
}