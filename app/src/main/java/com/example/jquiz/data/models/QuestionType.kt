package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class QuestionType(
    @PrimaryKey
    val code : Int,
    val typeDifficulty : Int
)