package com.example.jquiz.interfaces

import androidx.room.Dao
import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec

interface QuestionGenerator<T> {

    suspend fun  generate(spec : QuestionSpec, dao: DataSource<T>) : Question?

}