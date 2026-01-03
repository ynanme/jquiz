package com.example.jquiz.dto

import android.R
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind

abstract class Question {
    abstract val field: Fields
    abstract val type: QuestionKind
    abstract val difficulty: Int
}


data class QCM(
    override val field: Fields,
    override val type: QuestionKind,
    override val difficulty: Int,
    val label: String,
    val answer: String,
    val choices: List<String>
): Question()


data class DynamicSortItem (
    val label: String,
    val value: Int
)

data class DynamicSortQuestion(
    override val field: Fields,
    override val type: QuestionKind,
    override val difficulty: Int,
    val label: String,
    val items: List<DynamicSortItem>
): Question()


data class PrecisionQuestion(
    override val field: Fields,
    override val type: QuestionKind,
    override val difficulty: Int,
    val label: String,
    val answer: String,
    val objectName: String
) : Question()

data class MatchQuestion(
    override val field: Fields,
    override val type: QuestionKind,
    override val difficulty: Int,
    val label: String,
    val items: List<MatchItem>
) : Question()

data class MatchItem(
    val name: String,
    val value: String
)
