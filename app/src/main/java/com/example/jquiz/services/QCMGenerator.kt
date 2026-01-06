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


        var elements = dao.selectRandomRows(1, spec.difficulty.value.first, spec.difficulty.value.last)
        if (elements.isEmpty()) {
           elements = dao.selectRandomRows(1, Difficulty.EASY.value.first, Difficulty.HARD.value.last)
        }

        if (elements.isEmpty()) return null

        val correctElement = elements.random()

        val difficulty = correctElement::class.memberProperties.find { it.name == "notoriety" }?.getter?.call(correctElement).toString().toInt()

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
            difficulty,
            "${column.name} of $element",
            answer,
            choices
        )

    }

}