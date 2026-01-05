package com.example.jquiz.viewmodels

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.jquiz.services.Frequency
import com.example.jquiz.services.NotificationsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val Context.notificationsDataStore: DataStore<Preferences> by preferencesDataStore(name = "notifications")


class NotificationsViewModel (application: Application): AndroidViewModel(application) {

    val reminder = NotificationsManager(application.notificationsDataStore)
    var frequency = reminder.getFrequencyFlow()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun setFrequency(newFrequency: Frequency) {
        viewModelScope.launch {
            reminder.setFrequency(newFrequency)
        }
        reminder.schedule(newFrequency, application)
    }
    fun setPermissions (newGranted: Boolean) {
        viewModelScope.launch { reminder.setPermissions(newGranted) }
    }

}