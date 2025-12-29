package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
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


        val elements = dao.selectRandomRows(50, 0)
        if (elements.size < spec.count) return null
        val candidates = dao.selectRandomRows(50, spec.difficulty.value)

        val correctElement = candidates.random()

        val columns = correctElement::class.memberProperties
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in listOf("id", "notoriety") }
        val objectName = correctElement::class.memberProperties
            .find { it.findAnnotation<ObjectName>() != null }

        if (columns.isEmpty()) return null

        val column = columns.random()

        val answer = column.getter.call(correctElement).toString()
        val element = objectName?.getter?.call(correctElement).toString()

        val otherChoices = elements
            .filter { it != correctElement && column.getter.call(it) != column.getter.call(correctElement) }
            .map { column.getter.call(it).toString() }
            .distinct()
            .take(spec.count - 1)

        val choices = (otherChoices + answer).shuffled()


        return QCM(
            spec.field,
            QuestionKind.QCM,
            spec.difficulty.value,
            "${column.name} of $element",
            answer,
            choices
        )

    }

}