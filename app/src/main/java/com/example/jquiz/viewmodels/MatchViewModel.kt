package com.example.jquiz.viewmodels

import android.app.Application
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
import com.example.jquiz.dto.MatchItem
import com.example.jquiz.dto.MatchQuestion
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.dto.QuizResult
import com.example.jquiz.services.MatchGenerator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MatchViewModel(application: Application, domains: List<Fields>, level: Difficulty, ) :
    QuizViewModel(application, domains, level)
{
    override val questionsCount = when (level) {
        Difficulty.EASY -> 4
        Difficulty.MEDIUM -> 6
        Difficulty.HARD -> 8
    }

    private val _question = MutableStateFlow<MatchQuestion?>(null)
    val question: StateFlow<MatchQuestion?> = _question
    var remainingMatches = questionsCount

    fun generateQuestion() {
        val spec = QuestionSpec(level, domains.random(), count = questionsCount)
        viewModelScope.launch {
            val generatedQuestion = when (spec.field) {
                Fields.CHEMICAL_ELEMENT -> {
                    val generator = MatchGenerator<ChemicalElement>()
                    generator.generate(spec, chemicalElementsDao)
                }
                Fields.PLANET -> {
                    val generator = MatchGenerator<Planet>()
                    generator.generate(spec, planetsDao)
                }
                Fields.HISTORIC_EVENT -> {
                    val generator = MatchGenerator<HistoricEvent>()
                    generator.generate(spec, historicEventsDao)
                }
                Fields.FOOTBALL_TEAM -> {
                    val generator = MatchGenerator<FootballTeam>()
                    generator.generate(spec, footballTeamsDao)
                }
                Fields.COUNTRY -> {
                    val generator = MatchGenerator<Country>()
                    generator.generate(spec, countriesDao)
                }
            }
            _question.value = generatedQuestion as? MatchQuestion
        }
    }

     fun evaluate(userAnswer: MatchItem) {
        if(userAnswer in _question.value!!.items) correctAnswersCount++
         remainingMatches-=1
    }


    override fun reset() {
        _question.value = null
        quizIsOver.value = false
        correctAnswersCount = 0
        generateQuestion()
    }

    fun getQuizResult(): QuizResult {
        return QuizResult(correctAnswersCount, questionsCount, QuestionKind.MATCH, level, domains)
    }

    fun endGame() {
        quizIsOver.value = true
        saveQuestion()
        updateGame()
    }

    fun saveQuestion() {
        val gameQuestion = GameQuestions(
            gameId = game.id,
            isCorrect = correctAnswersCount == questionsCount,
            questionData = "",
            correctAnswer = "",
            userAnswer = "",
        )

    }


    override fun getQuestionKind(): QuestionKind {
        return QuestionKind.MATCH

    }
}