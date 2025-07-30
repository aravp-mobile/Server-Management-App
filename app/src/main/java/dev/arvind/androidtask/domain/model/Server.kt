package dev.arvind.androidtask.domain.model

data class Server(
    val id: String,
    val name: String?,
    val type: ServerType,
    val region: String,
    val state: ServerState,
    val ipAddress: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val lastBillingTimestamp: Long,
    val totalBilling: Double,
    val uptime: Long = 0
)