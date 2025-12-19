package com.example.jquiz.data.daos

interface SeedableDao<T> {

    suspend fun count() : Int
    suspend fun insertAll(items : List<T>): List<Long>
}