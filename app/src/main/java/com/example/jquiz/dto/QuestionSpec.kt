package com.example.jquiz.dto

import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields

data class QuestionSpec (
    val difficulty: Difficulty,
    val field: Fields,
    val count : Int,

)