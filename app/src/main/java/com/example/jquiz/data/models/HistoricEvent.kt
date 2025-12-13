package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HistoricEvent (
    @PrimaryKey
    val id: Int,
    val event: String,
    val date: String,
    val notoriety: Int
)

