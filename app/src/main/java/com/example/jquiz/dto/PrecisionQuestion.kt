package com.example.jquiz.dto

import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind

class PrecisionQuestion(
    override val field: Fields,
    override val type: QuestionKind,
    override val difficulty: Int,
    val label: String,
    val answer: String,
    val objectName: String
) : Question()
