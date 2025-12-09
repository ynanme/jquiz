package com.example.jquiz.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.Country
import kotlinx.coroutines.flow.Flow


@Dao
interface  CountriesDAO {

    @Insert
    suspend fun insertCountry (country: Country): Long

    @Query("SELECT * FROM Country")
    fun loadAll (): Flow<List<Country>>

}