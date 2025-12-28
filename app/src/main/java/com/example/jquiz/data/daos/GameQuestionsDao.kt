package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.GameQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface GameQuestionsDao {

    @Insert
    suspend fun AddGameQuestion(gameQuestion : GameQuestions)

    @Query("SELECT * FROM GameQuestions WHERE Id = :id")
    suspend fun GetGameQuestionById(id : Int)

    @Query("SELECT * FROM GameQuestions")
    fun LoadAllGameQuestions() : Flow<List<GameQuestions>>

    @Delete
    suspend fun Delete(id : Int);

    @Query("SELECT * FROM GameQuestions WHERE gameId = :gameId")
    fun LoadGameQuestionsByGameId(gameId : Int) : Flow<List<GameQuestions>>

}