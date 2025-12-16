package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.daos.SeedableDao
import kotlinx.coroutines.flow.Flow

@Dao
interface  HistoricEventsDao : SeedableDao<HistoricEvent>{

    @Insert
    suspend fun insertEvent (event: HistoricEvent): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<HistoricEvent>): List<Long>

    @Query("SELECT * FROM HistoricEvent")
    fun loadAll (): Flow<List<HistoricEvent>>

    @Query("SELECT COUNT(*) FROM HistoricEvent")
    override suspend fun count(): Int


}