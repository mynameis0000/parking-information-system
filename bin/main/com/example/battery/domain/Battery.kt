package com.example.battery.domain

import com.example.battery.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "battery")
class Battery(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    var station: Station?,

    @Column(nullable = false, unique = true)
    val serialNumber: String,

    @Column(nullable = false)
    var batteryLevel: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: BatteryStatus = BatteryStatus.AVAILABLE
) : BaseEntity() {

    fun rent() {
        check(status == BatteryStatus.AVAILABLE) { "대여 가능한 상태의 배터리가 아닙니다." }
        this.status = BatteryStatus.RENTED
        this.station = null
    }
}