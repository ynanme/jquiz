package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jquiz.data.annotations.ObjectName
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class ChemicalElement (
    @PrimaryKey
    val id: Int,
    @ObjectName
    val element: String,
    val atomicNumber: Int,
    val atomicMass: Int,
    val notoriety: Int
)

