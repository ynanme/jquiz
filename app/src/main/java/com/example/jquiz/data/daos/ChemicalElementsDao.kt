package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.daos.SeedableDao
import com.example.jquiz.data.models.Country
import com.example.jquiz.interfaces.DataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface  ChemicalElementsDao : DataSource<ChemicalElement>, SeedableDao<ChemicalElement>{

    @Insert
    suspend fun insertElement (element: ChemicalElement): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<ChemicalElement>): List<Long>

    @Query("SELECT * FROM ChemicalElement")
    fun loadAll (): Flow<List<ChemicalElement>>

    @Query("SELECT COUNT(*) FROM ChemicalElement")
    override suspend fun count(): Int

    @Query("""SELECT * FROM ChemicalElement WHERE notoriety >= :difficulty ORDER BY RANDOM() LIMIT :n""")
    override suspend fun selectRandomRows(n: Int, difficulty: Int): List<ChemicalElement>


}