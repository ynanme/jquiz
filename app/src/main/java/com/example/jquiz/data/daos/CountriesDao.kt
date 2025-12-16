package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.daos.SeedableDao
import kotlinx.coroutines.flow.Flow

@Dao
interface  CountriesDao : SeedableDao<Country>{

    @Insert
    suspend fun insertCountry (country: Country): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<Country>): List<Long>

    @Query("SELECT * FROM Country")
    fun loadAll (): Flow<List<Country>>

    @Query("SELECT COUNT(*) FROM Country")
    override suspend fun count(): Int


}