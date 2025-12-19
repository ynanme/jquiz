package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.daos.SeedableDao
import com.example.jquiz.data.models.Country
import com.example.jquiz.interfaces.DataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface  FootballTeamsDao : DataSource<FootballTeam>,SeedableDao<FootballTeam>{

    @Insert
    suspend fun insertTeam (team: FootballTeam): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<FootballTeam>): List<Long>

    @Query("SELECT * FROM FootballTeam")
    fun loadAll (): Flow<List<FootballTeam>>

    @Query("SELECT COUNT(*) FROM FootballTeam")
    override suspend fun count(): Int

    @Query("""SELECT * FROM FootballTeam WHERE notoriety >= :difficulty ORDER BY RANDOM() LIMIT :n""")
    override suspend fun selectRandomRows(n: Int, difficulty: Int): List<FootballTeam>

}