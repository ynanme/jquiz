package com.example.jquiz.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.services.Color
import com.example.jquiz.services.Font
import com.example.jquiz.viewmodels.PreferencesViewModel


@Composable
fun PreferencesScreen (model: PreferencesViewModel = viewModel()) {

    val darkTheme by model.darkThemeFlow.collectAsState(initial = false)
    val color by model.colorFlow.collectAsState(initial = Color.SYSTEM)
    val font by model.fontFlow.collectAsState(initial = Font.SYSTEM)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ThemeSection(darkTheme, { model.setDarkTheme(it) }, color, { model.setColor(it) })
        FontSection(font, { model.setFont(it) })
    }

}


@Composable
fun ThemeSection(darkTheme: Boolean, darkThemeSetter: (Boolean) -> Unit, color: Color, colorSetter: (Color) -> Unit) {
    Column {
        Text("Theme", style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dark")
            Spacer(Modifier.weight(1f))
            Switch(
                checked = darkTheme,
                onCheckedChange = { darkThemeSetter(it) }
            )
        }

        Color.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { colorSetter(option) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = color == option,
                    onClick = null
                )
                Text(option.name)
            }
        }
    }
}


@Composable
fun FontSection(font: Font, fontSetter: (Font) -> Unit) {
    Column {
        Text("Font", style = MaterialTheme.typography.titleMedium)

        Font.entries.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { fontSetter(option) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = font == option,
                    onClick = null
                )
                Text(option.name)
            }
        }
    }
}

