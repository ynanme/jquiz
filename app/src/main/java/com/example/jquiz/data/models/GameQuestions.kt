package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (foreignKeys = [
    ForeignKey(entity = Game::class,
    parentColumns = ["Id"],
    childColumns = ["GameId"],
    onDelete = ForeignKey.CASCADE),

        ForeignKey(entity = QuestionType::class,
            parentColumns = ["Id"],
            childColumns = ["TypeQuestionId"],
            onDelete = ForeignKey.CASCADE)
])
data class GameQuestions (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val dataId : Int,
    val totalDifficulty : Int,
    val score : Int,
    val field : String,
    val gameId : Int
)
