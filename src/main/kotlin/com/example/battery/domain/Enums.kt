package com.example.battery.domain

enum class BatteryStatus {
    AVAILABLE,  // 대여 가능
    RENTED,     // 대여 중
    DISCHARGED  // 방전/점검 필요
}

enum class RentalStatus {
    PROCEEDING, // 대여 진행 중
    RETURNED    // 반납 완료
}