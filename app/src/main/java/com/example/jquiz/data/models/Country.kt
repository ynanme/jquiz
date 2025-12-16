package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class Country (
    @PrimaryKey
    val id: Int,
    val country: String,
    val capital: String,
    val area: Int,
    val gdp: Int,
    val population: Int,
    val notoriety: Int
)

