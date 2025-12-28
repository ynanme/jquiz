package com.example.jquiz.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class AccountManager (private val account: DataStore<Preferences>) {

    private val QUIZ_COOLDOWN_DURATION = 1 * 60 * 1000

    val DEFAULT_USERNAME = "JQUIZER"
    val DEFAULT_IS_PREMIUM = false

    private val usernameKey = stringPreferencesKey("username")
    private val isPremiumKey = booleanPreferencesKey("isPremium")
    private val lastQuizTimestampKey = longPreferencesKey("lastQuizTimestamp")


    fun getUsernameFlow (): Flow<String> {
        return account.data.map { it[usernameKey] ?: DEFAULT_USERNAME }
    }

    fun getIsPremiumFlow (): Flow<Boolean> {
        return account.data.map { it[isPremiumKey] ?: DEFAULT_IS_PREMIUM }
    }

    suspend fun updateUsername (newUsername: String) {
        account.edit { it[usernameKey] = newUsername }
    }

    suspend fun updatePremiumStatus (isPremium: Boolean) {
        account.edit { it[isPremiumKey] = isPremium }
    }

    suspend fun remainingTime (): Long {
        val last = account.data.first()[lastQuizTimestampKey] ?: 0L
        if (last == 0L) return 0L
        val now = System.currentTimeMillis()
        val elapsed = now - last
        val remaining = QUIZ_COOLDOWN_DURATION - elapsed
        return remaining.coerceAtLeast(0L)

    }

    suspend fun canQuiz (): Boolean {
        val isPremium = account.data.first()[isPremiumKey] ?: DEFAULT_IS_PREMIUM
        //return isPremium || remainingTime() == 0L
        return true
    }

    suspend fun registerNewQuiz () {
        account.edit {
            it[lastQuizTimestampKey] = System.currentTimeMillis()
        }
    }

}