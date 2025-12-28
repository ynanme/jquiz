package com.example.jquiz.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.traceEventEnd
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.dto.DynamicSortQuestion
import com.example.jquiz.dto.QuizResult


class DynamicSortViewModel (application: Application, domains: List<Fields>, level: Difficulty): QuizViewModel(application, domains, level) {

    var question = DynamicSortQuestion(
        "temperature",
        listOf(
            DynamicSortItem("Mercury", 167, 5),
            DynamicSortItem("Venus", 464, 6),
            DynamicSortItem("Terre", 15, 4),
            DynamicSortItem("Mars", -63, 3),
            DynamicSortItem("Jupiter", -110, 2),
            DynamicSortItem("Saturn", -140, 1)
        )
    )

    private var remainingItems = mutableStateListOf<DynamicSortItem>().apply { addAll(question.items) }
    var sortedItems = mutableStateListOf(getNextItem())
    var itemToSort = mutableStateOf(getNextItem())
    var evaluations = mutableStateMapOf<String, Boolean?>(sortedItems[0]!!.label to true)



    private fun getNextItem (): DynamicSortItem? {
        if (remainingItems.isNotEmpty()) {
            val item = remainingItems.random()
            remainingItems.remove(item)
            return item
        }
        return null
    }

    fun trySort (index: Int) {
        sortedItems.add(index, itemToSort.value!!)
        sortedItems.sortBy { it?.value }
        val evaluation = sortedItems.indexOf(itemToSort.value) == index
        evaluations[itemToSort.value!!.label] = evaluation
        if (evaluation) correctAnswersCount += 1
        if (remainingItems.isNotEmpty()) itemToSort.value = getNextItem() else quizIsOver.value = true
    }

    fun itemHasBeenInsertedCorrectly (item: DynamicSortItem): Boolean? {
        return evaluations[item.label]
    }

    fun getQuizResult (): QuizResult {
        return QuizResult(correctAnswersCount, questionsCount, QuestionKind.DYNAMIC_SORT, level, domains)
    }

    override fun reset () {
        quizIsOver.value = false
        correctAnswersCount = 0
        remainingItems = mutableStateListOf<DynamicSortItem>().apply { addAll(question.items) }
        sortedItems = mutableStateListOf(getNextItem())
        itemToSort = mutableStateOf(getNextItem())
        evaluations = mutableStateMapOf<String, Boolean?>(sortedItems[0]!!.label to true)
    }

    override fun getQuestionKind(): QuestionKind {
        return QuestionKind.DYNAMIC_SORT
    }

}