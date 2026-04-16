package com.queuemanager.app.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): com.queuemanager.app.domain.repository.AuthRepository {
        return com.queuemanager.app.data.repository.AuthRepositoryImpl(auth, firestore)
    }

    @Provides
    @Singleton
    fun provideBusinessRepository(firestore: FirebaseFirestore): com.queuemanager.app.domain.repository.BusinessRepository {
        return com.queuemanager.app.data.repository.BusinessRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideQueueRepository(firestore: FirebaseFirestore): com.queuemanager.app.domain.repository.QueueRepository {
        return com.queuemanager.app.data.repository.QueueRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideDiscoverRepository(firestore: FirebaseFirestore): com.queuemanager.app.domain.repository.DiscoverRepository {
        return com.queuemanager.app.data.repository.DiscoverRepositoryImpl(firestore)
    }
}
