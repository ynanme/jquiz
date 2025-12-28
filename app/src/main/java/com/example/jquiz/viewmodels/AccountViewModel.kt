package com.example.jquiz.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.services.AccountManager
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class AccountViewModel (application: Application): AndroidViewModel(application) {

    private val accountManager = AccountManager(application.dataStore)

    val DEFAULT_USERNAME = accountManager.DEFAULT_USERNAME
    val DEFAULT_IS_PREMIUM = accountManager.DEFAULT_IS_PREMIUM

    val usernameFlow = accountManager.getUsernameFlow()
    val isPremiumFlow = accountManager.getIsPremiumFlow()

    fun updateUsername (newUsername: String) {
        viewModelScope.launch {
            accountManager.updateUsername(newUsername)
        }
    }

    fun updatePremiumStatus (isPremium: Boolean) {
        viewModelScope.launch {
            accountManager.updatePremiumStatus(isPremium)
        }
    }

}