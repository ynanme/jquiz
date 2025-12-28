package com.example.jquiz.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jquiz.ui.theme.Typography
import com.example.jquiz.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(vm : SettingsViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text("Settings screen !", style = Typography.displayLarge)
        val isVip by vm.isVip.collectAsState()

        Switch(
            checked = isVip,
            onCheckedChange = { vm.toggleVip(it) }
        )
    }
}