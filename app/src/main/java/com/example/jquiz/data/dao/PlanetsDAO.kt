package com.example.jquiz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.Planet
import kotlinx.coroutines.flow.Flow


@Dao
interface  PlanetsDAO {

    @Insert
    suspend fun insertPlanet (planet: Planet): Long

    @Query("SELECT * FROM Planet")
    fun loadAll (): Flow<List<Planet>>

}