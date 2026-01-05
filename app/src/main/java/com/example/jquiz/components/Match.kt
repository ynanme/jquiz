package com.example.jquiz.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jquiz.dto.MatchItem
import com.example.jquiz.viewmodels.MatchViewModel

@Composable
fun Match(viewModel: MatchViewModel, onPrevious: () -> Unit) {
    if (viewModel.isQuizOver()) {
        QuizRecapBox(
            result = viewModel.getQuizResult(),
            onRetry = { viewModel.reset() },
            onBack = onPrevious
        )
    }
    else {
        MatchQuiz(viewModel)
    }
}

@Composable
fun MatchQuiz(vm: MatchViewModel) {

    val question = vm.question.collectAsState().value

    var selectedName by remember { mutableStateOf<String?>(null) }
    var selectedValue by remember { mutableStateOf<String?>(null) }

    val disabledNames = remember { mutableStateOf<Set<String>>(emptySet()) }
    val disabledValues = remember { mutableStateOf<Set<String>>(emptySet()) }

    if (question == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val leftItems = remember(question) { question.items.map { it.name }.shuffled() }
    val rightItems = remember(question) { question.items.map { it.value }.shuffled() }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Match : ${question.label}",
            style = MaterialTheme.typography.titleMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

            Column(modifier = Modifier.weight(1f)) {
                leftItems.forEach { name ->
                    MatchCard(
                        text = name,
                        selected = selectedName == name,
                        enabled = name !in disabledNames.value,
                        onClick = {
                            selectedName = if (selectedName == name) {
                                null
                            } else {
                                name
                            }
                        }
                    )
                }
            }


            Column(modifier = Modifier.weight(1f)) {
                rightItems.forEach { value ->
                    MatchCard(
                        text = value,
                        selected = selectedValue == value,
                        enabled = value !in disabledValues.value,
                        onClick = {
                            selectedValue = value
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(selectedName, selectedValue) {
        if (selectedName != null && selectedValue != null) {

            vm.evaluate(MatchItem(selectedName!!, selectedValue!!))

            disabledNames.value += selectedName!!
            disabledValues.value += selectedValue!!

            selectedName = null
            selectedValue = null

            if (vm.remainingMatches == 0) {
                vm.endGame()
            }
        }
    }
}


@Composable
fun MatchCard(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .let {
                if (enabled) it.clickable { onClick() } else it
            },
        colors = CardDefaults.cardColors(
            containerColor = when {
                !enabled -> MaterialTheme.colorScheme.surfaceVariant
                selected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            fontWeight = FontWeight.Medium,
            color = if (enabled)
                MaterialTheme.colorScheme.onSurface
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


