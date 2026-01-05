package com.example.jquiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jquiz.dto.QCM
import com.example.jquiz.viewmodels.PrecisionViewModel
import com.example.jquiz.viewmodels.QCMViewModel


@Composable
fun QCM(model: QCMViewModel, onQuit: () -> Unit) {


    if (!model.isQuizOver()){
        QCMQuestionBox(model.getCurrentQuestion(), model.getCurrentGivenAnswer(), { model.giveAnswer(it) }) {model.saveQuestion() ;model.goToNextQuestion() }
    } else {
        model.updateGame(true)
        QuizRecapBox(model.getQCMResult(), { model.reset() }, { onQuit() })
    }

}

@Composable
fun QCMQuestionBox (
    question: QCM,
    chosen: String,
    choose: (String) -> Unit,
    submit: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = question.label,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.Companion.height(8.dp))

            Choices(question.choices, chosen, question.answer) { choose(it) }

            Spacer(Modifier.Companion.height(8.dp))

            Button (
                onClick = submit,
                enabled = chosen != "",
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}



@Composable
fun Choices (choices: List<String>, chosen: String, answer: String, choose: (String) -> Unit) {
    Column (verticalArrangement = Arrangement.spacedBy(12.dp)) {
        choices.forEach { choice ->
            ChoiceCard(
                choice,
                choice == chosen,
                choice == answer && chosen != ""
            ) { choose(choice) }
        }
    }
}


@Composable
fun ChoiceCard (
    text: String,
    selected: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    val color = when {
        isCorrect -> Color(0xFF66BB6A)
        !isCorrect && selected -> Color(0xFFEF5350)
        else -> MaterialTheme.colorScheme.surface
    }

    ElevatedCard(
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



