package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.daos.SeedableDao
import com.example.jquiz.interfaces.DataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface  CountriesDao : DataSource<Country>, SeedableDao<Country> {

    @Insert
    suspend fun insertCountry (country: Country): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<Country>): List<Long>

    @Query("SELECT * FROM Country")
    fun loadAll (): Flow<List<Country>>

    @Query("SELECT COUNT(*) FROM Country")
    override suspend fun count(): Int

    @Query("""SELECT * FROM Country WHERE notoriety BETWEEN :min AND :max ORDER BY RANDOM() LIMIT :n""")
    override suspend fun selectRandomRows(n: Int, min: Int, max : Int): List<Country>
}