package com.queuemanager.app.domain.repository

import com.queuemanager.app.domain.model.Business
import kotlinx.coroutines.flow.Flow

interface DiscoverRepository {
    fun getAllBusinesses(): Flow<List<Business>>
    fun searchBusinesses(query: String): Flow<List<Business>>
    fun getBusinessesByCategory(category: String): Flow<List<Business>>
}
