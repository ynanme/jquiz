package com.example.jquiz.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.jquiz.data.annotations.ObjectName
import kotlinx.serialization.Serializable


@Entity
@Serializable
data class Planet (
    @PrimaryKey
    val id: Int,
    @ObjectName
    val planet: String,
    val mass: Int,
    val diameter: Int,
    val position: Int,
    val temperature: Int,
    val notoriety: Int
)

