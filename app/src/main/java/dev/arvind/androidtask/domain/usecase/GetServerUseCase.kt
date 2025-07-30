package dev.arvind.androidtask.domain.usecase

import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetServersUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    operator fun invoke(): Flow<List<Server>> {
        return repository.getAllServers()
    }
}