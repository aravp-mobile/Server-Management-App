package dev.arvind.androidtask.domain.usecase

import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.repository.ServerRepository
import javax.inject.Inject

class UpdateServerStateUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(serverId: String, newState: ServerState): Result<Unit> {
        return repository.updateServerState(serverId, newState)
    }
}