package com.example.jquiz.interfaces

import androidx.annotation.IntRange
import com.example.jquiz.data.models.Country

interface DataSource<T> {
    suspend fun selectRandomRows(n : Int, min : Int, max : Int) : List<T>;
}