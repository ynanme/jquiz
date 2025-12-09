package com.example.jquiz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.ChemicalElement
import kotlinx.coroutines.flow.Flow


@Dao
interface  ChemicalElementsDAO {

    @Insert
    suspend fun insertElement (element: ChemicalElement): Long

    @Query("SELECT * FROM ChemicalElement")
    fun loadAll (): Flow<List<ChemicalElement>>

}