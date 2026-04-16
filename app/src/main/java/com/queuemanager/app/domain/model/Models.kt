package com.queuemanager.app.domain.model

import com.google.firebase.Timestamp

enum class UserRole {
    CUSTOMER, BUSINESS_OWNER
}

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.CUSTOMER,
    val profileImage: String? = null,
    val createdAt: Timestamp = Timestamp.now()
)

data class Business(
    val id: String = "",
    val ownerId: String = "",
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val address: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val qrCode: String = "",
    val rating: Float = 0f,
    val createdAt: Timestamp = Timestamp.now()
)

data class Service(
    val id: String = "",
    val businessId: String = "",
    val name: String = "",
    val avgDuration: Int = 15, // in minutes
    val isAvailable: Boolean = true
)

enum class QueueStatus {
    ACTIVE, PAUSED, CLOSED
}

data class Queue(
    val id: String = "",
    val serviceId: String = "",
    val date: String = "", // Format: YYYY-MM-DD
    val status: QueueStatus = QueueStatus.ACTIVE,
    val currentTicketNumber: Int = 0
)

enum class ReservationStatus {
    RESERVED, SERVED, CANCELLED, NO_SHOW, DELAYED
}

data class Reservation(
    val id: String = "",
    val queueId: String = "",
    val customerId: String = "",
    val ticketNumber: Int = 0,
    val status: ReservationStatus = ReservationStatus.RESERVED,
    val eta: Timestamp? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val servedAt: Timestamp? = null
)
