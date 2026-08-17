package com.example.battery.domain

import com.example.battery.domain.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "station")
class Station(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var latitude: Double,

    @Column(nullable = false)
    var longitude: Double,

    @Column(nullable = false)
    var totalSlots: Int,

    @Column(nullable = false)
    var availableCount: Int
) : BaseEntity() {

    fun decreaseAvailableCount() {
        check(availableCount > 0) { "대여 가능한 배터리가 없습니다." }
        this.availableCount--
    }

    fun increaseAvailableCount() {
        check(availableCount < totalSlots) { "거치대가 이미 가득 차 있습니다." }
        this.availableCount++
    }
}