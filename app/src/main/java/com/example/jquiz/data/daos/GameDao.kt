package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.jquiz.data.models.Game
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert
    suspend fun AddGame(game : Game)

    @Query("SELECT * FROM Game WHERE Id = :id")
    suspend fun GetGameById(id : Int) : Game

    @Query("SELECT * FROM Game")
    fun LoadAllGames() : Flow<List<Game>>

    @Update
    suspend fun update(game: Game)
}