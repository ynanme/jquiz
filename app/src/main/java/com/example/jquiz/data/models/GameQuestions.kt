package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (foreignKeys = [
    ForeignKey(entity = Game::class,
    parentColumns = ["id"],
    childColumns = ["gameId"],
    onDelete = ForeignKey.CASCADE)
])
data class GameQuestions (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val gameId : Int,
    val questionData : String,
    val correctAnswer : String,
    val userAnswer : String,
    val isCorrect : Boolean,
)
