package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

@Entity
data class Game (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val finalScore: Int,
    val difficulty : Int,
    val endedGame : Boolean,
    val date : Date
)