package dev.arvind.androidtask.presentation.screen.provision

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.arvind.androidtask.domain.model.ServerType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvisionScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProvisionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle errors
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Provision Server") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::updateName,
                label = { Text("Server Name (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            ServerTypeSelection(
                selectedType = uiState.selectedType,
                onTypeSelected = viewModel::updateServerType
            )

            RegionSelection(
                selectedRegion = uiState.selectedRegion,
                onRegionSelected = viewModel::updateRegion
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.provisionServer(onNavigateBack)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isProvisioning
            ) {
                if (uiState.isProvisioning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Provisioning...")
                } else {
                    Text("Provision Server")
                }
            }
        }
    }
}

@Composable
private fun ServerTypeSelection(
    selectedType: ServerType?,
    onTypeSelected: (ServerType) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Server Type",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ServerType.values().forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedType == type,
                        onClick = { onTypeSelected(type) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = type.displayName,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${type.hourlyRate}/hour",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RegionSelection(
    selectedRegion: String,
    onRegionSelected: (String) -> Unit
) {
    val regions = listOf(
        "us-east-1",
        "us-west-2",
        "eu-west-1",
        "ap-southeast-1"
    )

    Card {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Region",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            regions.forEach { region ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedRegion == region,
                        onClick = { onRegionSelected(region) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = region,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}