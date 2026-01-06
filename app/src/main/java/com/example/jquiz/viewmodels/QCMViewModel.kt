package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.GameQuestions
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.models.Planet
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.dto.QuizResult
import com.example.jquiz.services.QCMGenerator
import com.example.jquiz.services.Translator
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class QCMViewModel (application: Application, domains: List<Fields>, level: Difficulty
): QuizViewModel(application, domains, level) {


    private var currentQuestionIndex = mutableIntStateOf(1)
    private var givenAnswer = mutableStateOf("")
    private var question = mutableStateOf<QCM>(
        QCM(
            domains.random(),
            QuestionKind.QCM,
            Difficulty.MEDIUM.value.average().toInt(),
            "Question",
            "Answer",
            listOf("Choices")
        )
    )
    private var evaluated = mutableStateOf(false)



    fun getCurrentQuestion(): QCM {
        return question.value
    }

    fun getCurrentGivenAnswer(): String {
        return givenAnswer.value
    }


    fun giveAnswer(answer: String) {
        if (!evaluated.value) {
            givenAnswer.value = answer
            evaluated.value = true
        }
    }

    fun goToNextQuestion() {
        evaluateCurrentAnswer()
        if (currentQuestionIndex.intValue < questionsCount) {
            currentQuestionIndex.intValue += 1
            givenAnswer.value = ""
            generateQuestion()
        } else quizIsOver.value = true
    }


    fun getQCMResult(): QuizResult {
        return QuizResult(correctAnswersCount, questionsCount, QuestionKind.QCM, level, domains)
    }

    override fun reset() {
        quizIsOver.value = false
        currentQuestionIndex.intValue = 1
        givenAnswer.value = ""
        correctAnswersCount = 0
        viewModelScope.launch {
            launchGame()
            generateQuestion()
        }
    }

    override fun getQuestionKind(): QuestionKind {
        return QuestionKind.QCM
    }

    private fun evaluateCurrentAnswer () {
        if (givenAnswer.value == getCurrentQuestion().answer) correctAnswersCount += 1
    }

    fun saveQuestion() {
        val gameQuestion = GameQuestions(
            gameId = game.id,
            isCorrect = givenAnswer.value == question.value.answer,
            questionData = Json.encodeToString(value =question.value.label),
            correctAnswer = Json.encodeToString(value =question.value.answer),
            userAnswer = Json.encodeToString(value =givenAnswer.value),
        )
        viewModelScope.launch {
            gameQuestionsDao.AddGameQuestion(gameQuestion)
        }
    }


    public override fun generateQuestion () {
        viewModelScope.launch {
            val domain = domains.random()
            val spec = QuestionSpec(level, domain, count = 4)

            val generatedQuestion = when (domain) {
                Fields.CHEMICAL_ELEMENT -> {
                    val generator = QCMGenerator<ChemicalElement>()
                    generator.generate(spec, chemicalElementsDao)
                }

                Fields.PLANET -> {
                    val generator = QCMGenerator<Planet>()
                    generator.generate(spec, planetsDao)
                }

                Fields.HISTORIC_EVENT -> {
                    val generator = QCMGenerator<HistoricEvent>()
                    generator.generate(spec, historicEventsDao)
                }

                Fields.FOOTBALL_TEAM -> {
                    val generator = QCMGenerator<FootballTeam>()
                    generator.generate(spec, footballTeamsDao)
                }

                Fields.COUNTRY -> {
                    val generator = QCMGenerator<Country>()
                    generator.generate(spec, countriesDao)
                }
            }
            val propertyName = generatedQuestion?.label?.substringBefore(" of ")
            val originalLabel = generatedQuestion?.label ?: "Question"
            val translatedProperty =
                Translator.getLabelOrNull(application.applicationContext, propertyName)
            if (translatedProperty != null) {
                generatedQuestion?.label =
                    originalLabel.replaceFirst(propertyName!!, translatedProperty)
            }
            question.value = (generatedQuestion as? QCM)!!
            evaluated.value = false
        }
    }
}