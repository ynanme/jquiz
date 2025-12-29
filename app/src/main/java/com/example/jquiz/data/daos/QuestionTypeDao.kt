package com.example.jquiz.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jquiz.data.models.QuestionType
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionTypeDao : SeedableDao<QuestionType> {

    @Insert
    suspend fun addQuestionType(qt : QuestionType)

    @Query("SELECT * FROM QuestionType")
    fun loadAllQuestionType() : Flow<List<QuestionType>>


    @Query("SELECT COUNT(*) FROM QuestionType")
    override suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override suspend fun insertAll(items: List<QuestionType>): List<Long>

}