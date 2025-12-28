package com.example.jquiz.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.data.models.*
import com.example.jquiz.dto.PrecisionQuestion
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.dto.QuizResult
import com.example.jquiz.services.PrecisionGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PrecisionViewModel(application: Application, domains: List<Fields>, level: Difficulty) :
    QuizViewModel(application, domains, level) {

    private val _question = MutableStateFlow<PrecisionQuestion?>(null)
    val question: StateFlow<PrecisionQuestion?> = _question

    var answeredQuestion = 0;

    fun generateQuestion() {

        viewModelScope.launch {
            val domain = domains.random()
            val spec = QuestionSpec(level, domain, count = 1)

            val generatedQuestion = when (domain) {
                Fields.CHEMICAL_ELEMENT -> {
                    val generator = PrecisionGenerator<ChemicalElement>()
                    generator.generate(spec, chemicalElementsDao)
                }
                Fields.PLANET -> {
                    val generator = PrecisionGenerator<Planet>()
                    generator.generate(spec, planetsDao)
                }
                Fields.HISTORIC_EVENT -> {
                    val generator = PrecisionGenerator<HistoricEvent>()
                    generator.generate(spec, historicEventsDao)
                }
                Fields.FOOTBALL_TEAM -> {
                    val generator = PrecisionGenerator<FootballTeam>()
                    generator.generate(spec, footballTeamsDao)
                }
                Fields.COUNTRY -> {
                    val generator = PrecisionGenerator<Country>()
                    generator.generate(spec, countriesDao)
                }
            }
            _question.value = generatedQuestion as? PrecisionQuestion
        }
    }

    override fun reset() {
        _question.value = null
    }

    override fun getQuestionKind(): QuestionKind {
        return QuestionKind.PRECISION
    }

    fun getPrecisionResult(): QuizResult {
        return QuizResult(correctAnswersCount, questionsCount, QuestionKind.PRECISION, level, domains)
    }

    fun evaluateAnswer(userAnswer: String) {
        if (userAnswer == _question.value?.answer){
            correctAnswersCount++
        }
        answeredQuestion++
        if (answeredQuestion == questionsCount) {
            quizIsOver.value = true
        }
    }

    fun saveQuestion(userAnswer : String) {
        val gameQuestion = GameQuestions(
            gameId = game.id,
            isCorrect = _question.value!!.answer == userAnswer,
            question = "What is the ${_question.value!!.label} of ${_question.value!!.objectName}?",
            correctAnswer = _question . value !!. answer,
            userAnswer = userAnswer,
            questionData = ""

        )
        viewModelScope.launch {
            gameQuestionsDao.AddGameQuestion(gameQuestion)
        }
    }
}
