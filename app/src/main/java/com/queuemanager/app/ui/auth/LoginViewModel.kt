package com.queuemanager.app.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login(onSuccess: (com.queuemanager.app.domain.model.UserRole) -> Unit) {
        if (_email.value.isBlank() || _password.value.isBlank()) {
            _error.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.signIn(_email.value, _password.value)
            
            if (result.isSuccess) {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser != null) {
                    val userDataResult = authRepository.getUserData(currentUser.uid)
                    _isLoading.value = false
                    if (userDataResult.isSuccess) {
                        val role = userDataResult.getOrNull()?.role ?: com.queuemanager.app.domain.model.UserRole.CUSTOMER
                        onSuccess(role)
                    } else {
                        _error.value = "Failed to fetch user profile"
                    }
                } else {
                    _isLoading.value = false
                    _error.value = "User session not found"
                }
            } else {
                _isLoading.value = false
                _error.value = result.exceptionOrNull()?.message ?: "Login failed"
            }
        }
    }
}
