package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Game (
    @PrimaryKey(autoGenerate = true)
    val Id : Int = 0,
    val FinalScore: Int,
    val Difficulty : Int,
    val EndedGame : Boolean,
    val Date : Date
)