package dev.arvind.androidtask.domain.usecase

import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.model.ServerType
import dev.arvind.androidtask.domain.repository.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class ProvisionServerUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(
        name: String?,
        type: ServerType,
        region: String
    ): Result<Server> {
        return try {
            val currentTime = System.currentTimeMillis()
            val server = Server(
                id = UUID.randomUUID().toString(),
                name = name?.takeIf { it.isNotBlank() },
                type = type,
                region = region,
                state = ServerState.PENDING,
                ipAddress = null,
                createdAt = currentTime,
                updatedAt = currentTime,
                lastBillingTimestamp = currentTime,
                totalBilling = 0.0
            )

            repository.insertServer(server)
            Result.success(server)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}