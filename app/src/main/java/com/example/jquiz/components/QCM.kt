package com.example.jquiz.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.jquiz.viewmodels.QCMViewModel


@Composable
fun QCM(model: QCMViewModel) {

    if (!model.qcmIsOver()) {
        val currentQuestion = model.getCurrentQuestion()
        Text(currentQuestion.label)
        Choices(currentQuestion.choices, model.getCurrentGivenAnswer()) { model.giveAnswer(it) }
        Text(model.getCurrentGivenAnswer())
    }

}


@Composable
fun Choices (choices: List<String>, chosen: String, choose: (String) -> Unit) {
    Column {
        choices.forEach { choice ->
            FilterChip(
                selected = choice == chosen,
                onClick = { choose(choice) },
                label = { Text(choice) }
            )
        }
    }
}