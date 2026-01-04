package com.example.jquiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind



@Composable
fun QuizLevelChooser(
    fields: List<Pair<Difficulty, String>>,
    selected: Difficulty,
    levelSelector: (Difficulty) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fields.forEach { (difficulty, label) ->
            FilterChip(
                selected = difficulty == selected,
                onClick = { levelSelector(difficulty) },
                label = { Text(label) }
            )
        }
    }
}


@Composable
fun QuizTypeChooser (
    typeFields: List<Pair<QuestionKind, String>>,
    levelFields: List<Pair<Difficulty, String>>,
    typeSelector: (QuestionKind) -> Unit,
    levelSelector: (Difficulty) -> Unit,
    selectedLevel: Difficulty
) {
    Column(
        modifier = Modifier.Companion
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        QuizLevelChooser(levelFields, selectedLevel, levelSelector)
        Spacer(Modifier.Companion.height(8.dp))
        typeFields.forEach { (type, label) ->
            QuizParamCard(label, { typeSelector(type) }, false)
        }

    }
}


@Composable
fun QuizDomainsChooser (
    fields: List<Pair<Fields, String>>,
    domainsSelector: (Fields) -> Unit,
    domainIsChosen: (Fields) -> Boolean,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    Column(
        modifier = Modifier.Companion.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.Companion
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            fields.forEach { (domain, label) ->
                item { QuizParamCard(label, { domainsSelector(domain) }, domainIsChosen(domain)) }
            }
        }
        Button(onClick = onNext, modifier = Modifier.Companion.fillMaxWidth().padding(16.dp)) { Text("Go ->") }
        Button(onClick = onPrevious, modifier = Modifier.Companion.fillMaxWidth().padding(16.dp)) { Text("<- Back") }
    }
}


@Composable
fun QuizParamCard (
    title: String,
    setter: () -> Unit,
    selected: Boolean
) {
    val color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ElevatedCard(
        onClick = setter,
        colors = CardDefaults.elevatedCardColors(containerColor = color),
        modifier = Modifier.Companion
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier.Companion.fillMaxSize(),
            contentAlignment = Alignment.Companion.Center
        ) {
            Text(title)
        }
    }
}