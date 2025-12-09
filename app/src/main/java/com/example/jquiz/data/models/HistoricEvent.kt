package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity
data class HistoricEvent (
    @PrimaryKey
    val id: Int,
    val event: String,
    val date: Date,
    val notoriety: Int
)

