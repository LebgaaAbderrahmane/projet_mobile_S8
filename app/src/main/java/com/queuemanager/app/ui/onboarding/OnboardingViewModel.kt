package com.queuemanager.app.ui.onboarding

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.model.Business
import com.queuemanager.app.domain.repository.AuthRepository
import com.queuemanager.app.domain.repository.BusinessRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val businessRepository: BusinessRepository
) : ViewModel() {

    private val _currentStep = mutableStateOf(1)
    val currentStep: State<Int> = _currentStep

    // Step 1: Basic Info
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _category = mutableStateOf("")
    val category: State<String> = _category

    private val _address = mutableStateOf("")
    val address: State<String> = _address

    // Step 2: Location & Hours
    private val _lat = mutableStateOf(0.0)
    val lat: State<Double> = _lat

    private val _lng = mutableStateOf(0.0)
    val lng: State<Double> = _lng

    private val _openingHours = mutableStateOf("09:00 - 18:00")
    val openingHours: State<String> = _openingHours

    // Step 3: Services
    private val _services = mutableStateOf<List<com.queuemanager.app.domain.model.Service>>(emptyList())
    val services: State<List<com.queuemanager.app.domain.model.Service>> = _services

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun onNameChange(value: String) { _name.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onCategoryChange(value: String) { _category.value = value }
    fun onAddressChange(value: String) { _address.value = value }
    fun onLocationChange(lat: Double, lng: Double) {
        _lat.value = lat
        _lng.value = lng
    }
    fun onOpeningHoursChange(value: String) { _openingHours.value = value }

    fun addService(name: String, duration: Int) {
        val newService = com.queuemanager.app.domain.model.Service(
            name = name,
            avgDuration = duration
        )
        _services.value = _services.value + newService
    }

    fun removeService(service: com.queuemanager.app.domain.model.Service) {
        _services.value = _services.value - service
    }

    fun nextStep() {
        if (_currentStep.value < 4) {
            _currentStep.value += 1
        }
    }

    fun previousStep() {
        if (_currentStep.value > 1) {
            _currentStep.value -= 1
        }
    }

    fun saveStepData() {
        if (_currentStep.value == 1 && (_name.value.isBlank() || _address.value.isBlank())) {
            _error.value = "Name and Address are required"
            return
        }
        nextStep()
    }

    private var createdBusinessId: String? = null

    fun finishOnboarding(onFinish: () -> Unit) {
        if (_services.value.isEmpty()) {
            _error.value = "Please add at least one service"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val owner = authRepository.getCurrentUser()
            if (owner != null) {
                val businessId = createdBusinessId ?: UUID.randomUUID().toString()
                createdBusinessId = businessId
                val qrCode = "qmanager://business/$businessId"
                val business = Business(
                    id = businessId,
                    ownerId = owner.uid,
                    name = _name.value,
                    description = _description.value,
                    category = _category.value,
                    address = _address.value,
                    lat = _lat.value,
                    lng = _lng.value,
                    qrCode = qrCode
                )
                
                val result = businessRepository.createBusiness(business)
                if (result.isSuccess) {
                    // Add services
                    _services.value.forEach { service ->
                        businessRepository.addService(service.copy(businessId = businessId))
                    }
                    onFinish()
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            }
            _isLoading.value = false
        }
    }
}
