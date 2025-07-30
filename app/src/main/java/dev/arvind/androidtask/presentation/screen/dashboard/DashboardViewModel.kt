package dev.arvind.androidtask.presentation.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arvind.androidtask.domain.repository.ServerRepository
import dev.arvind.androidtask.domain.usecase.SyncServersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ServerRepository,
    private val syncServersUseCase: SyncServersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val serverCount = repository.getActiveServerCount()
                val totalBilling = repository.getTotalBilling()

                _uiState.value = _uiState.value.copy(
                    serverCount = serverCount,
                    totalBilling = totalBilling,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun syncWithFirestore() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSyncing = true)

            val result = syncServersUseCase()

            _uiState.value = _uiState.value.copy(
                isSyncing = false,
                syncSuccess = result.isSuccess,
                error = if (result.isFailure) result.exceptionOrNull()?.message else null
            )

            if (result.isSuccess) {
                loadDashboardData()
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class DashboardUiState(
    val serverCount: Int = 0,
    val totalBilling: Double = 0.0,
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val syncSuccess: Boolean = false,
    val error: String? = null
)