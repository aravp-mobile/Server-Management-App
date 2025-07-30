package dev.arvind.androidtask.presentation.screen.serverList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.usecase.GetServersUseCase
import dev.arvind.androidtask.domain.usecase.UpdateServerStateUseCase
import dev.arvind.androidtask.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val getServersUseCase: GetServersUseCase,
    private val updateServerStateUseCase: UpdateServerStateUseCase,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServerListUiState())
    val uiState: StateFlow<ServerListUiState> = _uiState.asStateFlow()

    init {
        loadServers()
    }

    private fun loadServers() {
        viewModelScope.launch {
            getServersUseCase().collect { servers ->
                _uiState.value = _uiState.value.copy(
                    servers = servers,
                    isLoading = false
                )
            }
        }
    }

    fun updateServerState(serverId: String, newState: ServerState) {
        viewModelScope.launch {
            val result = updateServerStateUseCase(serverId, newState)

            if (result.isSuccess) {
                notificationHelper.showNotification(
                    "Server State Changed",
                    "Server transitioned to ${newState.name}"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    error = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class ServerListUiState(
    val servers: List<Server> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)