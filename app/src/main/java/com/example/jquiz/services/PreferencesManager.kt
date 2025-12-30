package com.example.jquiz.services

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


enum class Color {
    BLUE,
    GREEN,
    SYSTEM
}

enum class Font {
    TAGESSCHRIFT,
    SAIRA,
    CHRISTMAS,
    SYSTEM
}



class PreferencesManager (private val preferences: DataStore<Preferences>) {

    private val darkThemeKey = booleanPreferencesKey("darkTheme")
    private val colorKey = stringPreferencesKey("color")
    private val fontKey = stringPreferencesKey("font")


    fun getDarkThemeFlow (): Flow<Boolean> {
        return preferences.data.map { it[darkThemeKey] ?: false }
    }

    fun getColorFlow (): Flow<Color> {
        return preferences.data.map { Color.valueOf(it[colorKey] ?: Color.SYSTEM.name)}
    }

    fun getFontFlow (): Flow<Font> {
        return preferences.data.map { Font.valueOf(it[fontKey] ?: Font.SYSTEM.name) }
    }

    suspend fun setDarkTheme (darkTheme: Boolean) {
        preferences.edit { it[darkThemeKey] = darkTheme }
    }

    suspend fun setColor (color: Color) {
        preferences.edit { it[colorKey] = color.name }
    }

    suspend fun setFont (font: Font) {
        preferences.edit { it[fontKey] = font.name }
    }

}
