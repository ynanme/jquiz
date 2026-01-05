package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.MatchItem
import com.example.jquiz.dto.MatchQuestion
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.interfaces.DataSource
import com.example.jquiz.interfaces.QuestionGenerator
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class MatchGenerator<T: Any>: QuestionGenerator<T> {

    override suspend fun generate(
        spec: QuestionSpec,
        dao: DataSource<T>
    ): Question? {
        val elements = dao.selectRandomRows(spec.count, spec.difficulty.value.first, spec.difficulty.value.last).toMutableList()
        if (elements.size < spec.count) {
            val remainingElements = dao.selectRandomRows(spec.count - elements.size, Difficulty.EASY.value.first,
                Difficulty.HARD.value.last)
            elements.addAll(remainingElements)
        }

        val meanDifficulty = elements.map { element ->
            val notorietyProperty = element::class.memberProperties.find { it.name == "notoriety" }
            (notorietyProperty?.getter?.call(element) as? Number)?.toDouble() ?: spec.difficulty.value.average()
        }.average().toInt()

        val row = elements.random()

        val allProps = row::class.memberProperties
        val answerProps = allProps
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in setOf("id", "notoriety" )}

        val objectNameProp = allProps.firstOrNull { it.findAnnotation<ObjectName>() != null } ?: return null

        val randomProp = answerProps.randomOrNull() ?: run {
            return null
        }
        val items = mutableListOf<MatchItem>()
        for(element in elements) {
            val value = randomProp.getter.call(element).toString()
            val name = objectNameProp.getter.call(element).toString()
            items.add(MatchItem(name, value))
        }


        return MatchQuestion(
            spec.field,
            QuestionKind.MATCH,
            meanDifficulty,
            label = randomProp.name,
            items = items,
        )

    }

}