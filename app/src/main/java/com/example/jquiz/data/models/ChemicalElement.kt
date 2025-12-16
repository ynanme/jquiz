package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class ChemicalElement (
    @PrimaryKey
    val id: Int,
    val element: String,
    val atomicNumber: Int,
    val atomicMass: Int,
    val notoriety: Int
)

