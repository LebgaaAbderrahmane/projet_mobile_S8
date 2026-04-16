package com.queuemanager.app.domain.repository

import com.queuemanager.app.domain.model.User
import com.queuemanager.app.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): User?
    fun isUserLoggedIn(): Boolean
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String, phone: String, role: UserRole): Result<Unit>
    suspend fun signOut()
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun getUserData(uid: String): Result<User?>
    suspend fun updateUserData(user: User): Result<Unit>
    suspend fun updatePassword(newPassword: String): Result<Unit>
}
