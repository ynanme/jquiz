package com.example.jquiz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.HistoricEvent
import kotlinx.coroutines.flow.Flow


@Dao
interface  HistoricEventsDAO {

    @Insert
    suspend fun insertEvent (event: HistoricEvent): Long

    @Query("SELECT * FROM HistoricEvent")
    fun loadAll (): Flow<List<HistoricEvent>>

}