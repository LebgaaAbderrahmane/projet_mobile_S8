package com.queuemanager.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.queuemanager.app.domain.model.User
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        // Note: Full user data would ideally be fetched from Firestore, 
        // but for immediate check we return a skeleton.
        return User(uid = firebaseUser.uid, email = firebaseUser.email ?: "")
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String,
        role: UserRole
    ): Result<Unit> {
        return try {
            android.util.Log.d("AuthRepository", "Starting signUp for $email")
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("User creation failed")
            android.util.Log.d("AuthRepository", "User created in Auth with UID: $uid")
            
            val user = User(
                uid = uid,
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                role = role
            )
            
            android.util.Log.d("AuthRepository", "Attempting to write user document to Firestore...")
            firestore.collection("users").document(uid).set(user).await()
            android.util.Log.d("AuthRepository", "Firestore write successful")
            Result.success(Unit)
        } catch (e: Throwable) {
            android.util.Log.e("AuthRepository", "SignUp error: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserData(uid: String): Result<User?> {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            val user = document.toObject(User::class.java)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserData(user: User): Result<Unit> {
        return try {
            firestore.collection("users").document(user.uid).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("No user logged in")
            user.updatePassword(newPassword).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
