package com.example.jquiz.screens

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.services.Frequency
import com.example.jquiz.viewmodels.NotificationsViewModel


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationsScreen (model: NotificationsViewModel = viewModel()) {

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { model.setPermissions(it) }

    val selected by model.frequency.collectAsState(initial = Frequency.NEVER)

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Remind me to play",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(8.dp))

        ReminderOption(
            label = "Every minute",
            selected = selected == Frequency.EVERY_MINUTE,
            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS); model.setFrequency(Frequency.EVERY_MINUTE) }
        )

        ReminderOption(
            label = "Every hour",
            selected = selected == Frequency.HOURLY,
            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS); model.setFrequency(Frequency.HOURLY) }
        )

        ReminderOption(
            label = "Every day",
            selected = selected == Frequency.DAILY,
            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS); model.setFrequency(Frequency.DAILY) }
        )

        ReminderOption(
            label = "Every week",
            selected = selected == Frequency.WEEKLY,
            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS); model.setFrequency(Frequency.WEEKLY) }
        )

        ReminderOption(
            label = "Never",
            selected = selected == Frequency.NEVER,
            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS); model.setFrequency(Frequency.NEVER) }
        )
    }
}


@Composable
fun ReminderOption (label: String, selected: Boolean, onClick: () -> Unit) {
    ElevatedCard (
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = null
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

