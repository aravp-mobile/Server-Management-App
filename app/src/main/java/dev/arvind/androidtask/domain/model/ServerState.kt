package dev.arvind.androidtask.domain.model

enum class ServerState {
    PENDING,
    RUNNING,
    STOPPED,
    TERMINATED;

    fun canTransitionTo(newState: ServerState): Boolean {
        return when (this) {
            PENDING -> newState in setOf(RUNNING, TERMINATED)
            RUNNING -> newState in setOf(STOPPED, TERMINATED)
            STOPPED -> newState in setOf(RUNNING, TERMINATED)
            TERMINATED -> false
        }
    }
}