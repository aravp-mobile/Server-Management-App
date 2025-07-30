package dev.arvind.androidtask.presentation.screen.serverDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.repository.ServerRepository
import dev.arvind.androidtask.domain.usecase.UpdateServerStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerDetailViewModel @Inject constructor(
    private val repository: ServerRepository,
    private val updateServerStateUseCase: UpdateServerStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServerDetailUiState())
    val uiState: StateFlow<ServerDetailUiState> = _uiState.asStateFlow()

    fun loadServer(serverId: String) {
        viewModelScope.launch {
            try {
                val server = repository.getServerById(serverId)
                _uiState.value = _uiState.value.copy(
                    server = server,
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

    fun updateServerState(newState: ServerState) {
        val server = _uiState.value.server ?: return

        viewModelScope.launch {
            val result = updateServerStateUseCase(server.id, newState)

            if (result.isFailure) {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message
                )
            }
            // Reload server to get updated state
            loadServer(server.id)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ServerDetailUiState(
    val server: Server? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)