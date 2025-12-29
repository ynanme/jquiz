package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jquiz.data.enums.QuestionKind

@Entity
data class Game (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var finalScore: Int,
    val difficulty: Int,
    val type: QuestionKind,
    var endedGame: Boolean,
    val date: String
)