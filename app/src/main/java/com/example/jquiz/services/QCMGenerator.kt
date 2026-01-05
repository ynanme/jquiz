package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.interfaces.DataSource
import com.example.jquiz.interfaces.QuestionGenerator
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


class QCMGenerator<T: Any>: QuestionGenerator<T> {

    override suspend fun generate(spec: QuestionSpec, dao: DataSource<T>): Question? {


        val elements = dao.selectRandomRows(spec.count, spec.difficulty.value.first, spec.difficulty.value.last).toMutableList()
        if (elements.size < spec.count) {
           elements.addAll(dao.selectRandomRows(spec.count - elements.size, Difficulty.EASY.value.first, Difficulty.HARD.value.last))
        }

        val correctElement = elements.random()

        val meanDifficulty = elements.map { element ->
            val notorietyProperty = element::class.memberProperties.find { it.name == "notoriety" }
            (notorietyProperty?.getter?.call(element) as? Number)?.toDouble() ?: spec.difficulty.value.average()
        }.average().toInt()

        val columns = correctElement::class.memberProperties
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in listOf("id", "notoriety") }
        val objectName = correctElement::class.memberProperties
            .find { it.findAnnotation<ObjectName>() != null }

        if (columns.isEmpty()) return null

        val column = columns.random()

        val answer = column.getter.call(correctElement).toString()
        val element = objectName?.getter?.call(correctElement).toString()

        val otherChoices = dao.selectRandomRows(50, Difficulty.EASY.value.first, Difficulty.HARD.value.last)
            .map { column.getter.call(it).toString() }
            .distinct()
            .filter { it != answer }
            .take(spec.count -1)

        val choices = (otherChoices + answer).shuffled()



        return QCM(
            spec.field,
            QuestionKind.QCM,
            meanDifficulty,
            "${column.name} of $element",
            answer,
            choices
        )

    }

}