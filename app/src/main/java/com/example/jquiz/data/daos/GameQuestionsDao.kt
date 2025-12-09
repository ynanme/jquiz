package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.GameQuestions

@Dao
interface GameQuestionsDao {

    @Insert
    suspend fun AddGameQuestion(gameQuestion : GameQuestions)

    @Query("SELECT * FROM GameQuestions WHERE Id = id")
    suspend fun GetGameQuestionById(id : Int)

    @Query("SELECT * FROM GameQuestions")
    suspend fun LoadAllGameQuestions();

    @Delete
    suspend fun Delete(id : Int);
}