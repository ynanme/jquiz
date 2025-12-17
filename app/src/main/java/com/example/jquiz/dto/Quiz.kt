package com.example.jquiz.dto

import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind


data class QuizResult(
    val score: Int,
    val total: Int,
    val type: QuestionKind,
    val difficulty: Difficulty,
    val domains: List<Fields>
)
