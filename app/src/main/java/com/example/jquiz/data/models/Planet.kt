package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class Planet (
    @PrimaryKey
    val id: Int,
    val planet: String,
    val mass: Int,
    val diameter: Int,
    val position: Int,
    val temperature: Int,
    val notoriety: Int
)

