package com.example.jquiz.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.jquiz.data.models.GameQuestions
import com.example.jquiz.dto.PrecisionQuestion
import com.example.jquiz.viewmodels.PrecisionViewModel

@Composable
fun Precision(vm: PrecisionViewModel, onQuit: () -> Unit) {
    var over by vm.quizIsOver
    val currentQuestion = vm.question.collectAsState()
    if (over) {
        vm.updateGame(true)
        QuizRecapBox(vm.getPrecisionResult(), { vm.reset() }, onQuit)
    }
    else {
        if(currentQuestion.value == null)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        else PrecisionQuestionBox(currentQuestion.value!!, vm)
    }
}

@Composable
fun PrecisionQuestionBox(question : PrecisionQuestion, vm: PrecisionViewModel) {
    var userAnswer by remember { mutableStateOf("")}
    Column() {
            Text("What is the ${question.label} of ${question.objectName}?",
                style = MaterialTheme.typography.headlineSmall)

        TextField(
            value = userAnswer,
            onValueChange = { userAnswer = it },
            label = { Text("Answer") }
        )
        Button(onClick = {
            vm.evaluateAnswer(userAnswer)
            vm.saveQuestion(userAnswer)
            userAnswer = ""
            vm.generateQuestion()
        }) {Text("Submit")}

    }

}