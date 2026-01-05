package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.daos.SeedableDao
import com.example.jquiz.data.models.Country
import com.example.jquiz.interfaces.DataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface  HistoricEventsDao : DataSource<HistoricEvent>,SeedableDao<HistoricEvent>{

    @Insert
    suspend fun insertEvent (event: HistoricEvent): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<HistoricEvent>): List<Long>

    @Query("SELECT * FROM HistoricEvent")
    fun loadAll (): Flow<List<HistoricEvent>>

    @Query("SELECT COUNT(*) FROM HistoricEvent")
    override suspend fun count(): Int

    @Query("""SELECT * FROM HistoricEvent WHERE notoriety BETWEEN :min AND :max ORDER BY RANDOM() LIMIT :n""")
    override suspend fun selectRandomRows(n: Int, min: Int, max : Int): List<HistoricEvent>

}