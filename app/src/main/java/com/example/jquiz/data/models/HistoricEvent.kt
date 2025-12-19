package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jquiz.data.annotations.ObjectName
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class HistoricEvent (
    @PrimaryKey
    val id: Int,
    @ObjectName
    val event: String,
    val date: Int,
    val notoriety: Int
)

