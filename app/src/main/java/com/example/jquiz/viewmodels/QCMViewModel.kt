package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.QuizResult


class QCMViewModel (application: Application, domains: List<Fields>, level: Difficulty): QuizViewModel(application, domains, level) {

    private val questionsCount = 5
    private val questions = listOf(
        QCM("Real Madrid C1s ?", "14", listOf("5", "0", "14", "3")),
        QCM("Oxygen atomic number ?", "8", listOf("6", "9", "7", "8")),
        QCM("France Capital ?", "Paris", listOf("Dublin", "Paris", "Dakar", "Lyon")),
        QCM("Hitler upcoming ?", "1929", listOf("1929", "1933", "1939", "2019")),
        QCM("Earth position in Solar System ?", "3", listOf("1", "0", "10", "3")),
    )

    private var currentQuestionIndex = mutableIntStateOf(0)
    private var qcmIsOver = mutableStateOf(false)
    private var givenAnswer = mutableStateOf("")

    private var correctAnswersCount = 0

    fun getCurrentQuestion (): QCM {
        return questions[currentQuestionIndex.intValue]
    }

    fun getCurrentGivenAnswer (): String {
        return givenAnswer.value
    }

    fun giveAnswer (answer: String) {
        givenAnswer.value = answer
    }

    fun goToNextQuestion () {
        evaluateCurrentAnswer()
        if (currentQuestionIndex.intValue < questions.size-1) {
            currentQuestionIndex.intValue += 1
            givenAnswer.value = ""
        } else qcmIsOver.value = true
    }

    fun qcmIsOver (): Boolean {
        return qcmIsOver.value
    }

    fun getQCMResult(): QuizResult {
        return QuizResult(correctAnswersCount, questionsCount, QuestionKind.QCM, level, domains)
    }

    fun reset () {
        qcmIsOver.value = false
        currentQuestionIndex.intValue = 0
        givenAnswer.value = ""
        correctAnswersCount = 0
    }

    private fun evaluateCurrentAnswer () {
        if (givenAnswer.value == getCurrentQuestion().answer) correctAnswersCount += 1
    }

}