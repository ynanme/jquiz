package com.example.jquiz.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind



@Composable
fun Quizer (
    chosenType: QuestionKind?,
    chosenLevel: Difficulty,
    chosenDomains: List<Fields>,
    onPrevious: () -> Unit
) {
    Column {
        Text("$chosenType")
        Text("$chosenLevel")
        chosenDomains.forEach { Text("$it") }
        Button(onClick = onPrevious) { Text("<- Back") }
    }
}