package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.Game

@Dao
interface GameDao {

    @Insert
    suspend fun AddGame(game : Game)

    @Query("SELECT * FROM Game WHERE Id = id")
    suspend fun GetGameById(id : Int);

    @Query("SELECT * FROM Game")
    suspend fun LoadAllGames()
}