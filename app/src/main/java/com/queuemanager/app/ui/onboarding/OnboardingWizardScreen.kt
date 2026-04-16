package com.queuemanager.app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun OnboardingWizardScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val currentStep by viewModel.currentStep
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress Indicator
        LinearProgressIndicator(
            progress = currentStep / 4f,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
        
        Text(
            text = "Step $currentStep of 4",
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (currentStep) {
            1 -> BasicInfoStep(viewModel)
            2 -> LocationStep(viewModel)
            3 -> ServicesStep(viewModel)
            4 -> ReviewStep(viewModel, onFinish)
        }
    }
}

@Composable
fun LocationStep(viewModel: OnboardingViewModel) {
    val lat by viewModel.lat
    val lng by viewModel.lng
    val hours by viewModel.openingHours

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Location & Hours", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lat.toString(),
            onValueChange = { viewModel.onLocationChange(it.toDoubleOrNull() ?: 0.0, lng) },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = lng.toString(),
            onValueChange = { viewModel.onLocationChange(lat, it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = hours,
            onValueChange = viewModel::onOpeningHoursChange,
            label = { Text("Opening Hours (e.g. 09:00 - 18:00)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { viewModel.previousStep() }) { Text("Back") }
            Button(onClick = { viewModel.saveStepData() }) { Text("Next") }
        }
    }
}

@Composable
fun ServicesStep(viewModel: OnboardingViewModel) {
    val services by viewModel.services
    var serviceName by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("15") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Your Services", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Add Service Section
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = serviceName,
                onValueChange = { serviceName = it },
                label = { Text("Service Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Mins") },
                modifier = Modifier.width(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                if (serviceName.isNotBlank()) {
                    viewModel.addService(serviceName, duration.toIntOrNull() ?: 15)
                    serviceName = ""
                }
            }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        services.forEach { service ->
            ListItem(
                headlineContent = { Text(service.name) },
                supportingContent = { Text("${service.avgDuration} minutes") },
                trailingContent = {
                    IconButton(onClick = { viewModel.removeService(service) }) {
                        Text("Delete")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { viewModel.previousStep() }) { Text("Back") }
            Button(onClick = { viewModel.saveStepData() }) { Text("Next") }
        }
    }
}

@Composable
fun ReviewStep(viewModel: OnboardingViewModel, onFinishCallback: () -> Unit) {
    val name by viewModel.name
    val address by viewModel.address
    val services by viewModel.services
    val isLoading by viewModel.isLoading

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Final Review", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Business: $name", style = MaterialTheme.typography.bodyLarge)
        Text("Address: $address", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Services:", style = MaterialTheme.typography.labelLarge)
        services.forEach { Text("- ${it.name} (${it.avgDuration} min)") }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.finishOnboarding(onFinishCallback) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp))
            else Text("Launch Business")
        }
        
        TextButton(onClick = { viewModel.previousStep() }, modifier = Modifier.fillMaxWidth()) {
            Text("Go Back")
        }
    }
}

@Composable
fun BasicInfoStep(viewModel: OnboardingViewModel) {
    val name by viewModel.name
    val description by viewModel.description
    val category by viewModel.category
    val address by viewModel.address
    val error by viewModel.error
    val isLoading by viewModel.isLoading

    Column {
        Text("Tell us about your business", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Business Name") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Short Description") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = address,
            onValueChange = viewModel::onAddressChange,
            label = { Text("Physical Address") },
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(text = error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveStepData() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Next")
            }
        }
    }
}
