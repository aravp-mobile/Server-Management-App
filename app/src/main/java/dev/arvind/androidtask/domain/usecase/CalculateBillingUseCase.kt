package dev.arvind.androidtask.domain.usecase

import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.repository.ServerRepository
import javax.inject.Inject

class CalculateBillingUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return try {
            val runningServers = repository.getServersByState(ServerState.RUNNING)
            val currentTime = System.currentTimeMillis()

            runningServers.forEach { server ->
                val billingSinceLastCheck = currentTime - server.lastBillingTimestamp
                val hoursRunning = billingSinceLastCheck / (1000.0 * 60 * 60)
                val additionalBilling = hoursRunning * server.type.hourlyRate

                val updatedServer = server.copy(
                    totalBilling = server.totalBilling + additionalBilling,
                    lastBillingTimestamp = currentTime,
                    uptime = server.uptime + billingSinceLastCheck,
                    updatedAt = currentTime
                )

                repository.updateServer(updatedServer)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}