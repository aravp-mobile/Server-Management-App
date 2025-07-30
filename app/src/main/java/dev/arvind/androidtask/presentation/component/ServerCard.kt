package dev.arvind.androidtask.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.arvind.androidtask.domain.model.Server
import dev.arvind.androidtask.domain.model.ServerState

@Composable
fun ServerCard(
    server: Server,
    onClick: () -> Unit,
    onStateChange: (ServerState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = server.name ?: "Unnamed Server",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${server.type.displayName} • ${server.region}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ServerStatusBadge(state = server.state)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "IP: ${server.ipAddress ?: "Not assigned"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Billing: ${String.format("%.2f", server.totalBilling)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ServerStatusBadge(state: ServerState) {
    val (color, text) = when (state) {
        ServerState.PENDING -> MaterialTheme.colorScheme.tertiary to "Booting"
        ServerState.RUNNING -> MaterialTheme.colorScheme.primary to "Running"
        ServerState.STOPPED -> MaterialTheme.colorScheme.secondary to "Stopped"
        ServerState.TERMINATED -> MaterialTheme.colorScheme.error to "Terminated"
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}