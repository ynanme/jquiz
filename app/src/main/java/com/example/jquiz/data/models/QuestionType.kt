package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jquiz.data.enums.QuestionKind

@Entity
data class QuestionType(
    @PrimaryKey(autoGenerate = true)
    val Id : Int = 0,
    val Code : QuestionKind,
    val TypeDifficulty : Int
)