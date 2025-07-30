package dev.arvind.androidtask.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.model.ServerType

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey
    val id: String,
    val name: String?,
    val type: String,
    val region: String,
    val state: String,
    val ipAddress: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val lastBillingTimestamp: Long,
    val totalBilling: Double,
    val uptime: Long
)

fun ServerEntity.toDomain(): Server = Server(
    id = id,
    name = name,
    type = ServerType.valueOf(type),
    region = region,
    state = ServerState.valueOf(state),
    ipAddress = ipAddress,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastBillingTimestamp = lastBillingTimestamp,
    totalBilling = totalBilling,
    uptime = uptime
)

fun Server.toEntity(): ServerEntity = ServerEntity(
    id = id,
    name = name,
    type = type.name,
    region = region,
    state = state.name,
    ipAddress = ipAddress,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastBillingTimestamp = lastBillingTimestamp,
    totalBilling = totalBilling,
    uptime = uptime
)