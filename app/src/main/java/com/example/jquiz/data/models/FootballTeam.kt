package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class FootballTeam (
    @PrimaryKey
    val id: Int,
    val club: String,
    val city: String,
    val yearOfFoundation: Int,
    val c1Count: Int,
    val euCount: Int,
    val notoriety: Int
)

