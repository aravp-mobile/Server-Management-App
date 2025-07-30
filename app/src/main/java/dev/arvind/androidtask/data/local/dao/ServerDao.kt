package dev.arvind.androidtask.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.arvind.androidtask.data.local.entities.ServerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServerDao {

    @Query("SELECT * FROM servers ORDER BY createdAt DESC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE id = :id")
    suspend fun getServerById(id: String): ServerEntity?

    @Query("SELECT * FROM servers WHERE state = :state")
    suspend fun getServersByState(state: String): List<ServerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: ServerEntity)

    @Update
    suspend fun updateServer(server: ServerEntity)

    @Transaction
    suspend fun updateServerState(serverId: String, newState: String, updatedAt: Long) {
        val server = getServerById(serverId)
        server?.let {
            updateServer(it.copy(state = newState, updatedAt = updatedAt))
        }
    }

    @Query("DELETE FROM servers WHERE id = :id")
    suspend fun deleteServer(id: String)

    @Query("SELECT COUNT(*) FROM servers WHERE state != 'TERMINATED'")
    suspend fun getActiveServerCount(): Int

    @Query("SELECT SUM(totalBilling) FROM servers")
    suspend fun getTotalBilling(): Double?
}