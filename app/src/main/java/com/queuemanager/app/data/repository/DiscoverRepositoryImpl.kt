package com.queuemanager.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.queuemanager.app.domain.model.Business
import com.queuemanager.app.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : DiscoverRepository {

    override fun getAllBusinesses(): Flow<List<Business>> {
        return firestore.collection("businesses")
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Business::class.java)
            }
    }

    override fun searchBusinesses(query: String): Flow<List<Business>> {
        // Firestore doesn't support full-text search directly without third-party.
        // For MVP, we'll fetch all and filter in memory or use a simple prefix match.
        return getAllBusinesses().map { list ->
            list.filter { it.name.contains(query, ignoreCase = true) }
        }
    }

    override fun getBusinessesByCategory(category: String): Flow<List<Business>> {
        return firestore.collection("businesses")
            .whereEqualTo("category", category)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(Business::class.java)
            }
    }
}
