package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.dto.QCM


class QCMViewModel (application: Application, domains: List<Fields>, level: Difficulty): QuizViewModel(application, domains, level) {

    private var qcmIsOver = mutableStateOf(false)
    private var givenAnswer = mutableStateOf("")

    fun getCurrentQuestion (): QCM {
        return QCM(
            "France Capital ?",
            "Paris",
            listOf("Dublin", "Paris", "Dakar", "Lyon")
        )
    }

    fun getCurrentGivenAnswer (): String {
        return givenAnswer.value
    }

    fun giveAnswer (answer: String) {
        givenAnswer.value = answer
    }

    fun qcmIsOver (): Boolean {
        return qcmIsOver.value
    }

}