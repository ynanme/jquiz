package com.example.jquiz.components

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.viewmodels.QuizViewModelFactory


@Composable
fun Quizer (
    chosenType: QuestionKind?,
    chosenLevel: Difficulty,
    chosenDomains: List<Fields>,
    onPrevious: () -> Unit
) {

    val application = LocalContext.current.applicationContext as Application

    when (chosenType) {

        QuestionKind.QCM -> QCM(viewModel(factory = QuizViewModelFactory(application, chosenDomains, chosenLevel)), onPrevious)

        else -> {
            Column {
                Text("Unsupported Quiz !")
                Button(onClick = onPrevious) { Text("<- Back") }
            }
        }

    }

}