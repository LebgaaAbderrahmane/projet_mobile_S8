package com.queuemanager.app.ui.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.model.User
import com.queuemanager.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = mutableStateOf<User?>(null)
    val user: State<User?> = _user

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    private val _isEditing = mutableStateOf(false)
    val isEditing: State<Boolean> = _isEditing

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            viewModelScope.launch {
                _isLoading.value = true
                val result = authRepository.getUserData(currentUser.uid)
                _isLoading.value = false
                if (result.isSuccess) {
                    _user.value = result.getOrNull()
                } else {
                    _error.value = "Failed to load profile"
                }
            }
        }
    }

    fun toggleEdit() {
        _isEditing.value = !_isEditing.value
    }

    fun updateProfile(firstName: String, lastName: String, phone: String) {
        val currentUser = _user.value ?: return
        val updatedUser = currentUser.copy(
            firstName = firstName,
            lastName = lastName,
            phone = phone
        )

        viewModelScope.launch {
            _isLoading.value = true
            val result = authRepository.updateUserData(updatedUser)
            _isLoading.value = false
            if (result.isSuccess) {
                _user.value = updatedUser
                _isEditing.value = false
            } else {
                _error.value = "Update failed"
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
            onLogout()
        }
    }
}
