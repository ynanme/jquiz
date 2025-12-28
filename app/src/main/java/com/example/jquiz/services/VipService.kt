package com.example.jquiz.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class VipService(context: Context) {
    private val dataStore = context.applicationContext.dataStore
    private val VIP_KEY = booleanPreferencesKey("vip")

    val vipFlow: Flow<Boolean> = dataStore.data
        .map { prefs -> prefs[VIP_KEY] ?: false }

    suspend fun setVip(value: Boolean) {
        dataStore.edit { prefs -> prefs[VIP_KEY] = value }
    }
}
