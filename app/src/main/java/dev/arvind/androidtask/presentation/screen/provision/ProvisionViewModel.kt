package dev.arvind.androidtask.presentation.screen.provision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arvind.androidtask.domain.model.ServerType
import dev.arvind.androidtask.domain.usecase.ProvisionServerUseCase
import dev.arvind.androidtask.worker.BootWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvisionViewModel @Inject constructor(
    private val provisionServerUseCase: ProvisionServerUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProvisionUiState())
    val uiState: StateFlow<ProvisionUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateServerType(type: ServerType) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun updateRegion(region: String) {
        _uiState.value = _uiState.value.copy(selectedRegion = region)
    }

    fun provisionServer(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.selectedType == null || state.selectedRegion.isBlank()) {
            _uiState.value = state.copy(error = "Please fill all required fields")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isProvisioning = true)

            val result = provisionServerUseCase(
                name = state.name.takeIf { it.isNotBlank() },
                type = state.selectedType,
                region = state.selectedRegion
            )

            if (result.isSuccess) {
                val server = result.getOrNull()!!

                // Schedule boot worker
                val bootWork = OneTimeWorkRequestBuilder<BootWorker>()
                    .setInputData(workDataOf("server_id" to server.id))
                    .build()

                workManager.enqueue(bootWork)

                _uiState.value = ProvisionUiState() // Reset state
                onSuccess()
            } else {
                _uiState.value = state.copy(
                    isProvisioning = false,
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ProvisionUiState(
    val name: String = "",
    val selectedType: ServerType? = null,
    val selectedRegion: String = "",
    val isProvisioning: Boolean = false,
    val error: String? = null
)