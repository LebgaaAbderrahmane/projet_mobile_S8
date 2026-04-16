package com.queuemanager.app.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuth()
    }

    private fun checkAuth() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            // We need to fetch the full user data to get the role
            viewModelScope.launch {
                val result = authRepository.getUserData(currentUser.uid)
                if (result.isSuccess) {
                    val user = result.getOrNull()
                    if (user != null) {
                        _authState.value = AuthState.Authenticated(user.role)
                    } else {
                        _authState.value = AuthState.Unauthenticated
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    sealed class AuthState {
        object Loading : AuthState()
        object Unauthenticated : AuthState()
        data class Authenticated(val role: UserRole) : AuthState()
    }
}
