package dev.arvind.androidtask.presentation.screen.serverDetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.arvind.androidtask.domain.model.ServerState
import dev.arvind.androidtask.utils.toDurationString
import dev.arvind.androidtask.utils.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerDetailScreen(
    serverId: String,
    onNavigateBack: () -> Unit,
    viewModel: ServerDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(serverId) {
        viewModel.loadServer(serverId)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.server?.name ?: "Server Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.server?.let { server ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Server Info Card
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Server Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            DetailRow("ID", server.id)
                            DetailRow("Type", server.type.displayName)
                            DetailRow("Region", server.region)
                            DetailRow("State", server.state.name)
                            DetailRow("IP Address", server.ipAddress ?: "Not assigned")
                            DetailRow("Created", server.createdAt.toFormattedDate())
                            DetailRow("Uptime", server.uptime.toDurationString())
                            DetailRow("Total Billing", "${String.format("%.2f", server.totalBilling)}")
                        }
                    }

                    // Actions Card
                    Card {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Actions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            ServerActionButtons(
                                currentState = server.state,
                                onStateChange = viewModel::updateServerState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ServerActionButtons(
    currentState: ServerState,
    onStateChange: (ServerState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (currentState) {
            ServerState.PENDING -> {
                Text(
                    text = "Server is booting...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            ServerState.RUNNING -> {
                Button(
                    onClick = { onStateChange(ServerState.STOPPED) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Stop Server")
                }
                OutlinedButton(
                    onClick = { onStateChange(ServerState.TERMINATED) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Terminate Server")
                }
            }
            ServerState.STOPPED -> {
                Button(
                    onClick = { onStateChange(ServerState.RUNNING) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Start Server")
                }
                OutlinedButton(
                    onClick = { onStateChange(ServerState.TERMINATED) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Terminate Server")
                }
            }
            ServerState.TERMINATED -> {
                Text(
                    text = "Server has been terminated",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}