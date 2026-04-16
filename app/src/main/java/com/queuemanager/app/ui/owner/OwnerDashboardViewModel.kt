package com.queuemanager.app.ui.owner

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.queuemanager.app.domain.model.Business
import com.queuemanager.app.domain.model.Queue
import com.queuemanager.app.domain.model.Reservation
import com.queuemanager.app.domain.repository.AuthRepository
import com.queuemanager.app.domain.repository.BusinessRepository
import com.queuemanager.app.domain.repository.QueueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OwnerDashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val businessRepository: BusinessRepository,
    private val queueRepository: QueueRepository
) : ViewModel() {

    private val _business = mutableStateOf<Business?>(null)
    val business: State<Business?> = _business

    private val _queue = mutableStateOf<Queue?>(null)
    val queue: State<Queue?> = _queue

    private val _reservations = mutableStateOf<List<Reservation>>(emptyList())
    val reservations: State<List<Reservation>> = _reservations

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            val owner = authRepository.getCurrentUser()
            if (owner != null) {
                val businessResult = businessRepository.getBusinessByOwner(owner.uid)
                if (businessResult.isSuccess) {
                    val business = businessResult.getOrNull()
                    _business.value = business
                    
                    if (business != null) {
                        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        // Listen to queue
                        queueRepository.getQueue(business.id, today).collectLatest { queue ->
                            _queue.value = queue
                            if (queue != null) {
                                // Listen to reservations
                                queueRepository.getActiveReservations(queue.id).collectLatest { reservations ->
                                    _reservations.value = reservations
                                }
                            }
                        }
                    }
                }
            }
            _isLoading.value = false
        }
    }

    fun callNext() {
        val queueId = _queue.value?.id ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val result = queueRepository.callNextCustomer(queueId)
            if (result.isFailure) {
                _error.value = result.exceptionOrNull()?.message
            }
            _isLoading.value = false
        }
    }
}
