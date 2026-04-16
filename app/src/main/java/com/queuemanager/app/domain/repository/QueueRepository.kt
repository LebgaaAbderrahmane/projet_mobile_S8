package com.queuemanager.app.domain.repository

import com.queuemanager.app.domain.model.Queue
import com.queuemanager.app.domain.model.QueueStatus
import com.queuemanager.app.domain.model.Reservation
import com.queuemanager.app.domain.model.ReservationStatus
import kotlinx.coroutines.flow.Flow

interface QueueRepository {
    fun getQueue(businessId: String, date: String): Flow<Queue?>
    fun getActiveReservations(queueId: String): Flow<List<Reservation>>
    suspend fun updateReservationStatus(reservationId: String, status: ReservationStatus): Result<Unit>
    suspend fun updateQueueStatus(queueId: String, status: QueueStatus): Result<Unit>
    suspend fun callNextCustomer(queueId: String): Result<Unit>
}
