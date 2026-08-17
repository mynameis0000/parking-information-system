package com.example.battery.domain

import com.example.battery.domain.common.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "rental")
class Rental(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battery_id", nullable = false)
    val battery: Battery,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_station_id", nullable = false)
    val rentStation: Station,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: RentalStatus = RentalStatus.PROCEEDING,

    val rentedAt: LocalDateTime = LocalDateTime.now(),

    var returnedAt: LocalDateTime? = null
) : BaseEntity()