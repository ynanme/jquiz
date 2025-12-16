package com.example.jquiz.interfaces

import com.example.jquiz.dto.Question
import com.example.jquiz.dto.QuestionSpec

interface QuestionGenerator {

    fun generate(spec : QuestionSpec) : Question
}