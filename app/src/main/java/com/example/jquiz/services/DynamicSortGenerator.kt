package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.dto.DynamicSortQuestion
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.interfaces.DataSource
import com.example.jquiz.interfaces.QuestionGenerator
import kotlin.collections.distinctBy
import kotlin.collections.toMutableList
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


class DynamicSortGenerator<T: Any>: QuestionGenerator<T> {

    override suspend fun generate(spec: QuestionSpec, dao: DataSource<T>): Question? {


        var elements  = dao.selectRandomRows(spec.count-1, spec.difficulty.value.first, spec.difficulty.value.last)
        if (elements.size < spec.count) {
            elements = elements.toMutableList()
            val remainingElements = dao.selectRandomRows(spec.count - elements.size, Difficulty.EASY.value.first, Difficulty.HARD.value.last)
            elements.addAll(remainingElements)
        }

        val meanDifficulty = elements.map { element ->
            val notorietyProperty = element::class.memberProperties.find { it.name == "notoriety" }
            (notorietyProperty?.getter?.call(element) as? Number)?.toDouble() ?: spec.difficulty.value.average()
        }.average().toInt()

        val randomElement = elements.random()

        val columns = randomElement::class.memberProperties
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in listOf("id", "notoriety") }
            .filter { it.getter.call(randomElement) is Number }
        val objectName = randomElement::class.memberProperties
            .find { it.findAnnotation<ObjectName>() != null }

        if (columns.isEmpty()) return null

        val column = columns.random()

        val items = elements.map { DynamicSortItem(objectName?.getter?.call(it).toString(), column.getter.call(it) as Int) }

        return DynamicSortQuestion(
            spec.field,
            QuestionKind.DYNAMIC_SORT,
            meanDifficulty,
            "Sort by ${column.name}",
            items
        )

    }

}