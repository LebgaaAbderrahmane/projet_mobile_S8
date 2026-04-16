package com.queuemanager.app.ui.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.model.UserRole
import com.queuemanager.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _firstName = mutableStateOf("")
    val firstName: State<String> = _firstName

    private val _lastName = mutableStateOf("")
    val lastName: State<String> = _lastName

    private val _email = mutableStateOf("")
    val email: State<String> = _email

    private val _phone = mutableStateOf("")
    val phone: State<String> = _phone

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private val _role = mutableStateOf(UserRole.CUSTOMER)
    val role: State<UserRole> = _role

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun onFirstNameChange(value: String) { _firstName.value = value }
    fun onLastNameChange(value: String) { _lastName.value = value }
    fun onEmailChange(value: String) { _email.value = value }
    fun onPhoneChange(value: String) { _phone.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onRoleChange(value: UserRole) { _role.value = value }

    fun signUp(onSuccess: (com.queuemanager.app.domain.model.UserRole) -> Unit) {
        if (_email.value.isBlank() || _password.value.isBlank() || _firstName.value.isBlank() || _lastName.value.isBlank()) {
            _error.value = "Please fill in all required fields"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val roleValue = _role.value
                val result = authRepository.signUp(
                    _email.value, 
                    _password.value, 
                    _firstName.value, 
                    _lastName.value, 
                    _phone.value, 
                    roleValue
                )
                
                if (result.isSuccess) {
                    onSuccess(roleValue)
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Sign up failed"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
