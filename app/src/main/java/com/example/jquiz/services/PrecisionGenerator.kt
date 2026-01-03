package com.example.jquiz.services


import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.PrecisionQuestion
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec
import com.example.jquiz.interfaces.DataSource
import com.example.jquiz.interfaces.QuestionGenerator
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class PrecisionGenerator<T : Any> : QuestionGenerator<T> {

    override suspend fun generate(spec: QuestionSpec, dao: DataSource<T>): Question? {

        val row: T = dao.selectRandomRows(spec.count, spec.difficulty.value).firstOrNull() ?: run {
            return null
        }

        val allProps = row::class.memberProperties

        val objectNameProp = allProps.firstOrNull { it.findAnnotation<ObjectName>() != null } ?: return null
        val objectName = objectNameProp.getter.call(row)?.toString() ?: "Inconnu"


        val answerProps = allProps
            .filter { it.findAnnotation<ObjectName>() == null }
            .filter { it.name !in setOf("id", "notoriety" )}

        val randomProp = answerProps.randomOrNull() ?: run {
            return null
        }

        val v: String = randomProp.getter.call(row)?.toString() ?: ""
        val label: String = randomProp.name

        return PrecisionQuestion(
            spec.field,
            QuestionKind.PRECISION,
            spec.difficulty.value,
            answer = v,
            label = label,
            objectName = objectName
        )
    }
}
