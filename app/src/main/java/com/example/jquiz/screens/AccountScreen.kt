package com.example.jquiz.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.viewmodels.AccountViewModel


@Composable
fun AccountScreen (model: AccountViewModel = viewModel()) {

    val username by model.usernameFlow.collectAsState(initial = model.DEFAULT_USERNAME)
    val isPremium by model.isPremiumFlow.collectAsState(initial = model.DEFAULT_IS_PREMIUM)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box (
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        AccountUsernameCard(
            username = username,
            onUsernameChange = { model.updateUsername(it) }
        )

        Spacer(Modifier.height(32.dp))

        AccountStatusCard(
            isPremium = isPremium,
            onUpgrade = { model.updatePremiumStatus(it) }
        )
    }

}


@Composable
fun AccountUsernameCard (username: String, onUsernameChange: (String) -> Unit) {

    var isEditing by remember { mutableStateOf(false) }
    var editValue by remember { mutableStateOf(username) }

    LaunchedEffect (username) { editValue = username }

    ElevatedCard (modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Username",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = editValue,
                    onValueChange = { editValue = it },
                    singleLine = true,
                    trailingIcon = {
                        Row {
                            IconButton(onClick = {
                                onUsernameChange(editValue)
                                isEditing = false
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Save")
                            }
                            IconButton(onClick = {
                                editValue = username
                                isEditing = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Cancel")
                            }
                        }
                    }
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { isEditing = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit username")
                    }
                }
            }
        }
    }
}



@Composable
fun AccountStatusCard (isPremium: Boolean, onUpgrade: (Boolean) -> Unit) {
    ElevatedCard (
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(
                text = "Account",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (isPremium) "Status: Premium" else "Status: Free"
            )

            Spacer(Modifier.height(16.dp))

            Button (
                onClick = { onUpgrade(!isPremium) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isPremium) "Downgrade to Free" else "Upgrade to Premium")
            }
        }
    }
}

