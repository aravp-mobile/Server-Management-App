package dev.arvind.androidtask.domain.repository

import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getAllServers(): Flow<List<Server>>
    suspend fun getServerById(id: String): Server?
    suspend fun insertServer(server: Server)
    suspend fun updateServerState(serverId: String, newState: ServerState): Result<Unit>
    suspend fun updateServer(server: Server)
    suspend fun deleteServer(id: String)
    suspend fun getActiveServerCount(): Int
    suspend fun getTotalBilling(): Double
    suspend fun syncWithFirestore(): Result<Unit>
    suspend fun getServersByState(state: ServerState): List<Server>
}