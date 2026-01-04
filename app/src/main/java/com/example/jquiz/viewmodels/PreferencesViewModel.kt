package com.example.jquiz.viewmodels

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.services.Color
import com.example.jquiz.services.Font
import com.example.jquiz.services.PreferencesManager
import kotlinx.coroutines.launch


val Context.preferencesDatastore: DataStore<Preferences> by preferencesDataStore(name = "preferences")



class PreferencesViewModel (application: Application): AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application.preferencesDatastore)

    val darkThemeFlow = preferencesManager.getDarkThemeFlow()
    val colorFlow = preferencesManager.getColorFlow()
    val fontFlow = preferencesManager.getFontFlow()


    fun setDarkTheme (darkTheme: Boolean) {
        viewModelScope.launch { preferencesManager.setDarkTheme(darkTheme) }
    }

    fun setColor (color: Color) {
        viewModelScope.launch { preferencesManager.setColor(color) }
    }

    fun setFont (font: Font) {
        viewModelScope.launch { preferencesManager.setFont(font) }
    }


}