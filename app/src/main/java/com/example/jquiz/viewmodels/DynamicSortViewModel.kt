package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
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
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.dto.DynamicSortQuestion
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.dto.QuizResult
import com.example.jquiz.services.DynamicSortGenerator
import com.example.jquiz.services.Translator
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random


class DynamicSortViewModel (application: Application, domains: List<Fields>, level: Difficulty,
                            override val questionsCount: Int = 5
): QuizViewModel(application, domains, level) {

    var question = mutableStateOf<DynamicSortQuestion?>(null)

    private var remainingItems = mutableStateListOf<DynamicSortItem>()
    var sortedItems = mutableStateListOf<DynamicSortItem>()
    var itemToSort = mutableStateOf<DynamicSortItem?>(null)
    var evaluations = mutableStateMapOf<String, Boolean?>()
    var userAnswers = mutableStateListOf<String>()




    private fun getNextItem (): DynamicSortItem? {
        return if (remainingItems.isNotEmpty()) {
            remainingItems.removeAt(Random.nextInt(remainingItems.size))
        } else{
            saveQuestion()
            updateGame()
            null
        }
    }

    fun trySort (index: Int) {
        sortedItems.add(index, itemToSort.value!!)
        userAnswers.add(index ,itemToSort.value!!.label)
        sortedItems.sortBy { it.value }
        val evaluation = sortedItems.indexOf(itemToSort.value) == index
        evaluations[itemToSort.value!!.label] = evaluation
        if (evaluation) correctAnswersCount += 1
        itemToSort.value = getNextItem()
    }

    fun itemHasBeenInsertedCorrectly (item: DynamicSortItem): Boolean? {
        return evaluations[item.label]
    }

    fun getQuizResult (): QuizResult {
        return QuizResult(correctAnswersCount,
            question.value?.items?.size?.minus(1) ?: questionsCount, QuestionKind.DYNAMIC_SORT, level, domains)
    }

    fun endQuiz () {
        quizIsOver.value = true
    }

    fun saveQuestion() {
        val sortedItemsLabels = sortedItems
            .sortedBy { it.value }
            .map { it.label }
        val data = question.value!!.items.map { it.label }
        val gameQuestion = GameQuestions(
            gameId = game.id,
            isCorrect = correctAnswersCount == questionsCount,
            questionData = Json.encodeToString(value = data),
            correctAnswer = Json.encodeToString(value = sortedItemsLabels),
            userAnswer = Json.encodeToString(value = userAnswers.toList())
        )
        viewModelScope.launch {
            gameQuestionsDao.AddGameQuestion(gameQuestion)
        }
    }

    override fun reset () {
        quizIsOver.value = false
        correctAnswersCount = 0
        viewModelScope.launch {
            launchGame()
        }
        generateQuestion()
    }

    private fun setupQuestion(question: DynamicSortQuestion) {
        remainingItems.clear()
        remainingItems.addAll(question.items)

        sortedItems.clear()
        evaluations.clear()

        itemToSort.value = getNextItem()

        itemToSort.value?.let { first ->
            sortedItems.add(first)
            evaluations[first.label] = true
            userAnswers.add(first.label)
            itemToSort.value = getNextItem()
        }
    }

    public override fun generateQuestion() {
        viewModelScope.launch {
            val domain = domains.random()
            val spec = QuestionSpec(level, domain, count = questionsCount+1)

            val generatedQuestion = when (domain) {
                Fields.CHEMICAL_ELEMENT -> {
                    val generator = DynamicSortGenerator<ChemicalElement>()
                    generator.generate(spec, chemicalElementsDao)
                }

                Fields.PLANET -> {
                    val generator = DynamicSortGenerator<Planet>()
                    generator.generate(spec, planetsDao)
                }

                Fields.HISTORIC_EVENT -> {
                    val generator = DynamicSortGenerator<HistoricEvent>()
                    generator.generate(spec, historicEventsDao)
                }

                Fields.FOOTBALL_TEAM -> {
                    val generator = DynamicSortGenerator<FootballTeam>()
                    generator.generate(spec, footballTeamsDao)
                }

                Fields.COUNTRY -> {
                    val generator = DynamicSortGenerator<Country>()
                    generator.generate(spec, countriesDao)
                }
            }
            if (generatedQuestion != null) {
                val propertyName = generatedQuestion.label.substringAfter(" of ")
                val originalLabel = generatedQuestion.label
                val translatedProperty =
                    Translator.getLabelOrNull(application.applicationContext, propertyName)
                if (translatedProperty != null) {
                    generatedQuestion.label =
                        originalLabel.replaceFirst(propertyName, translatedProperty)
                }
                question.value = (generatedQuestion as? DynamicSortQuestion)!!
                setupQuestion(generatedQuestion)
            }

        }
    }

    override fun getQuestionKind(): QuestionKind {
        return QuestionKind.DYNAMIC_SORT
    }

}