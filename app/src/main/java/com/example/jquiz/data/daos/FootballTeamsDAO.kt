package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.FootballTeam
import kotlinx.coroutines.flow.Flow

@Dao
interface  FootballTeamsDAO {

    @Insert
    suspend fun insertTeam (team: FootballTeam): Long

    @Query("SELECT * FROM FootballTeam")
    fun loadAll (): Flow<List<FootballTeam>>

}