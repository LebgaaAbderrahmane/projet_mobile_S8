package com.queuemanager.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.queuemanager.app.domain.model.Business
import com.queuemanager.app.domain.model.Service
import com.queuemanager.app.domain.repository.BusinessRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BusinessRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BusinessRepository {

    override suspend fun createBusiness(business: Business): Result<Unit> {
        return try {
            firestore.collection("businesses").document(business.id).set(business).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBusinessByOwner(ownerId: String): Result<Business?> {
        return try {
            val snapshot = firestore.collection("businesses")
                .whereEqualTo("ownerId", ownerId)
                .get()
                .await()
            val business = snapshot.documents.firstOrNull()?.toObject(Business::class.java)
            Result.success(business)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBusiness(business: Business): Result<Unit> {
        return try {
            firestore.collection("businesses").document(business.id).set(business).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addService(service: Service): Result<Unit> {
        return try {
            val docRef = firestore.collection("services").document()
            val finalService = service.copy(id = docRef.id)
            docRef.set(finalService).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServices(businessId: String): Result<List<Service>> {
        return try {
            val snapshot = firestore.collection("services")
                .whereEqualTo("businessId", businessId)
                .get()
                .await()
            val services = snapshot.toObjects(Service::class.java)
            Result.success(services)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
