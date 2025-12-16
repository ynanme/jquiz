package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.Planet
import com.example.jquiz.data.daos.SeedableDao
import kotlinx.coroutines.flow.Flow

@Dao
interface  PlanetsDao : SeedableDao<Planet>{

    @Insert
    suspend fun insertPlanet (planet: Planet): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<Planet>): List<Long>

    @Query("SELECT * FROM Planet")
    fun loadAll (): Flow<List<Planet>>

    @Query("SELECT COUNT(*) FROM Planet")
    override suspend fun count() : Int

}