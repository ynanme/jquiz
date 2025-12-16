package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuestionType(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val code : Int,
    val typeDifficulty : Int
)