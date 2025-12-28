// Dans HistoryScreen.kt

package com.example.jquiz.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jquiz.data.enums.GameFilter
import com.example.jquiz.data.models.Game
import com.example.jquiz.viewmodels.HistoryViewModel

@Composable
fun HistoryScreen(vm: HistoryViewModel) {
    val games by vm.filteredGames.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        FilterDropDown(onFilterSelected = { filter ->
            vm.setFilter(filter)
        })

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(games, key = { it.id }) { game -> // Utiliser une clé est une bonne pratique
                DrawGameCard(game)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropDown(onFilterSelected: (GameFilter) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Toutes") }
    val filterOptions = mapOf(
        "Toutes" to GameFilter.ALL,
        "Gagnées" to GameFilter.WON,
        "Perdues" to GameFilter.LOST
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.CenterEnd // Aligne le menu à droite
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrer par") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor() // Ancre le menu au champ de texte
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filterOptions.forEach { (text, filter) ->
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = {
                            selectedText = text
                            expanded = false
                            onFilterSelected(filter) // Appelle la fonction du ViewModel
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun DrawGameCard(game: Game) {
    // ... Le code de DrawGameCard reste inchangé
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Partie n°${game.id}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Score final :", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Joué le :", style = MaterialTheme.typography.bodyLarge)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${game.finalScore} pts",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = game.date,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
