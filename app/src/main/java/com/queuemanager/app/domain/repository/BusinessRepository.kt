package com.queuemanager.app.domain.repository

import com.queuemanager.app.domain.model.Business
import com.queuemanager.app.domain.model.Service

interface BusinessRepository {
    suspend fun createBusiness(business: Business): Result<Unit>
    suspend fun getBusinessByOwner(ownerId: String): Result<Business?>
    suspend fun updateBusiness(business: Business): Result<Unit>
    suspend fun addService(service: Service): Result<Unit>
    suspend fun getServices(businessId: String): Result<List<Service>>
}
