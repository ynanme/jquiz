package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.jquiz.data.models.QuestionType

@Dao
interface QuestionTypeDao {

    @Insert
    suspend fun AddQuestionType(qt : QuestionType)

    @Query("SELECT * FROM QuestionType WHERE Id = id")
    suspend fun GetQuestionTypeById(id : Int)

    @Query("SELECT * FROM QuestionType")
    suspend fun LoadAllQuestionType();
}