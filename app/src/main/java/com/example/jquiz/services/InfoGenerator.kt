package com.example.jquiz.services

import com.example.jquiz.data.annotations.ObjectName
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.dto.Info
import com.example.jquiz.interfaces.DataSource
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties


class InfoGenerator<T: Any> {

    suspend fun generate(dao: DataSource<T>): Info {

        val element = dao.selectRandomRows(1, Difficulty.EASY.value.first, Difficulty.HARD.value.last).first()

        val columns = element::class.memberProperties
            .filter { it.name !in listOf("id", "notoriety") }
            .filter { it.getter.call(element) is Number }
        val objectName = element::class.memberProperties
            .find { it.findAnnotation<ObjectName>() != null }


        return Info(
            objectName?.getter?.call(element).toString(),
            columns.associate { it.name to it.getter.call(element).toString() }
        )
    }

}