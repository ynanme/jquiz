package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.dto.DynamicSortQuestion
import com.example.jquiz.dto.QCM
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.interfaces.DataSource
import com.example.jquiz.interfaces.QuestionGenerator
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


class DynamicSortGenerator<T: Any>: QuestionGenerator<T> {

    override suspend fun generate(spec: QuestionSpec, dao: DataSource<T>): Question? {


        var elements = dao.selectRandomRows(50, spec.difficulty.value)
        if (elements.size < spec.count) elements = dao.selectRandomRows(50, 0)


        val randomElement = elements.random()

        val columns = randomElement::class.memberProperties
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in listOf("id", "notoriety") }
            .filter { it.getter.call(randomElement) is Number }
        val objectName = randomElement::class.memberProperties
            .find { it.findAnnotation<ObjectName>() != null }

        if (columns.isEmpty()) return null

        val column = columns.random()

        if (elements.distinctBy { column.getter.call(it)  }.size < spec.count) elements = dao.selectRandomRows(50, 0)

        val candidates = elements.distinctBy { column.getter.call(it)  }.take(spec.count)
        val items = candidates.map { DynamicSortItem(objectName?.getter?.call(it).toString(), column.getter.call(it) as Int) }

        return DynamicSortQuestion(
            spec.field,
            QuestionKind.DYNAMIC_SORT,
            spec.difficulty.value,
            "Sort by ${column.name}",
            items
        )

    }

}