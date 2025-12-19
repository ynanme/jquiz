package com.example.jquiz.interfaces

import com.example.jquiz.data.models.Country

interface DataSource<T> {
    suspend fun selectRandomRows(n : Int, difficulty: Int): List<T>;
}