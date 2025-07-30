package dev.arvind.androidtask.data.repository

import dev.arvind.androidtask.data.firebase.FirestoreService
import dev.arvind.androidtask.data.local.dao.ServerDao
import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.map
import dev.arvind.androidtask.data.local.entities.toDomain
import dev.arvind.androidtask.data.local.entities.toEntity

@Singleton
class ServerRepositoryImpl @Inject constructor(
    private val serverDao: ServerDao,
    private val firestoreService: FirestoreService
) : ServerRepository {

    private val stateMutex = Mutex()

    override fun getAllServers(): Flow<List<Server>> {
        return serverDao.getAllServers().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getServerById(id: String): Server? {
        return serverDao.getServerById(id)?.toDomain()
    }

    override suspend fun insertServer(server: Server) {
        serverDao.insertServer(server.toEntity())
    }

    override suspend fun updateServerState(serverId: String, newState: ServerState): Result<Unit> {
        return stateMutex.withLock {
            try {
                val currentServer = serverDao.getServerById(serverId)
                    ?: return Result.failure(Exception("Server not found"))

                val currentState = ServerState.valueOf(currentServer.state)

                if (!currentState.canTransitionTo(newState)) {
                    return Result.failure(
                        Exception("Invalid state transition: $currentState -> $newState")
                    )
                }

                val updatedAt = System.currentTimeMillis()
                serverDao.updateServerState(serverId, newState.name, updatedAt)

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateServer(server: Server) {
        serverDao.updateServer(server.toEntity())
    }

    override suspend fun deleteServer(id: String) {
        serverDao.deleteServer(id)
    }

    override suspend fun getActiveServerCount(): Int {
        return serverDao.getActiveServerCount()
    }

    override suspend fun getTotalBilling(): Double {
        return serverDao.getTotalBilling() ?: 0.0
    }

    override suspend fun syncWithFirestore(): Result<Unit> {
        return try {
            val localServers = serverDao.getAllServers()
            // For simplicity, we'll do a one-time fetch instead of flow
            val serversToSync = mutableListOf<Server>()
            localServers.collect { entities ->
                serversToSync.addAll(entities.map { it.toDomain() })
            }

            firestoreService.syncServers(serversToSync)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getServersByState(state: ServerState): List<Server> {
        return serverDao.getServersByState(state.name).map { it.toDomain() }
    }
}