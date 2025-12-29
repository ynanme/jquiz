package com.example.jquiz.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.traceEventEnd
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.models.Planet
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.dto.DynamicSortQuestion
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.dto.QuizResult
import com.example.jquiz.services.DynamicSortGenerator
import com.example.jquiz.services.QCMGenerator
import kotlinx.coroutines.launch
import kotlin.random.Random


class DynamicSortViewModel (application: Application, domains: List<Fields>, level: Difficulty): QuizViewModel(application, domains, level) {

    var question = mutableStateOf<DynamicSortQuestion?>(null)

    private var remainingItems = mutableStateListOf<DynamicSortItem>()
    var sortedItems = mutableStateListOf<DynamicSortItem>()
    var itemToSort = mutableStateOf<DynamicSortItem?>(null)
    var evaluations = mutableStateMapOf<String, Boolean?>()


    init { generate() }

    private fun getNextItem (): DynamicSortItem? {
        return if (remainingItems.isNotEmpty()) {
            remainingItems.removeAt(Random.nextInt(remainingItems.size))
        } else null
    }

    fun trySort (index: Int) {
        sortedItems.add(index, itemToSort.value!!)
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

    override fun reset () {
        quizIsOver.value = false
        correctAnswersCount = 0
        generate()
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
            itemToSort.value = getNextItem()
        }
    }

    private fun generate() {
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
                question.value = (generatedQuestion as? DynamicSortQuestion)!!
                setupQuestion(generatedQuestion)
            }

        }
    }

}