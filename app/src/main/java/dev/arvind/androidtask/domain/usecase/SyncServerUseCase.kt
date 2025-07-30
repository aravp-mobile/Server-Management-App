package dev.arvind.androidtask.domain.usecase

import dev.arvind.androidtask.domain.repository.ServerRepository
import javax.inject.Inject

class SyncServersUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncWithFirestore()
    }
}