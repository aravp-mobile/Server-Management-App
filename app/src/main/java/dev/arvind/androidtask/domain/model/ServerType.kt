package dev.arvind.androidtask.domain.model

enum class ServerType(val displayName: String, val hourlyRate: Double) {
    MICRO("Micro (1 vCPU, 1GB RAM)", 0.01),
    SMALL("Small (1 vCPU, 2GB RAM)", 0.02),
    MEDIUM("Medium (2 vCPU, 4GB RAM)", 0.04),
    LARGE("Large (4 vCPU, 8GB RAM)", 0.08)
}