package com.queuemanager.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.queuemanager.app.domain.model.Queue
import com.queuemanager.app.domain.model.QueueStatus
import com.queuemanager.app.domain.model.Reservation
import com.queuemanager.app.domain.model.ReservationStatus
import com.queuemanager.app.domain.repository.QueueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QueueRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : QueueRepository {

    override fun getQueue(businessId: String, date: String): Flow<Queue?> {
        return firestore.collection("queues")
            .whereEqualTo("businessId", businessId)
            .whereEqualTo("date", date)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.firstOrNull()?.toObject(Queue::class.java)
            }
    }

    override fun getActiveReservations(queueId: String): Flow<List<Reservation>> {
        return firestore.collection("reservations")
            .whereEqualTo("queueId", queueId)
            .whereEqualTo("status", ReservationStatus.RESERVED.name)
            .orderBy("ticketNumber", Query.Direction.ASCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Reservation::class.java)
            }
    }

    override suspend fun updateReservationStatus(
        reservationId: String,
        status: ReservationStatus
    ): Result<Unit> {
        return try {
            firestore.collection("reservations")
                .document(reservationId)
                .update("status", status.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateQueueStatus(queueId: String, status: QueueStatus): Result<Unit> {
        return try {
            firestore.collection("queues")
                .document(queueId)
                .update("status", status.name)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun callNextCustomer(queueId: String): Result<Unit> {
        return try {
            // In a real app, this would be a Firestore Transaction to ensure atomic increment
            // and status change. For now, we'll implement a simple version.
            val snapshot = firestore.collection("reservations")
                .whereEqualTo("queueId", queueId)
                .whereEqualTo("status", ReservationStatus.RESERVED.name)
                .orderBy("ticketNumber", Query.Direction.ASCENDING)
                .limit(1)
                .get()
                .await()
            
            val reservation = snapshot.documents.firstOrNull()?.toObject(Reservation::class.java)
            if (reservation != null) {
                updateReservationStatus(reservation.id, ReservationStatus.SERVED)
                // Also update current ticket in queue
                firestore.collection("queues").document(queueId)
                    .update("currentTicketNumber", reservation.ticketNumber)
                    .await()
                Result.success(Unit)
            } else {
                Result.failure(Exception("No customers in queue"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
