package com.example.jquiz.dto

import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.models.QuestionType

abstract class Question {
    abstract val field: Fields
    abstract val type: QuestionType
    abstract val difficulty: Int
}


data class QCM(
    val label: String,
    val answer: String,
    val choices: List<String>
)
