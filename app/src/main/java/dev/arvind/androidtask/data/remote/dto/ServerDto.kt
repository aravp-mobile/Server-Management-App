package dev.arvind.androidtask.data.remote.dto

import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.domain.model.ServerType

data class ServerDto(
    val id: String = "",
    val name: String? = null,
    val type: String = "",
    val region: String = "",
    val state: String = "",
    val ipAddress: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val lastBillingTimestamp: Long = 0L,
    val totalBilling: Double = 0.0,
    val uptime: Long = 0L
)

fun ServerDto.toDomain(): Server = Server(
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

fun Server.toDto(): ServerDto = ServerDto(
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