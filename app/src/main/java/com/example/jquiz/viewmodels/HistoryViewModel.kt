// Dans HistoryViewModel.kt

package com.example.jquiz.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.daos.GameDao
import com.example.jquiz.data.enums.GameFilter
import com.example.jquiz.data.models.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine


class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val gamesDao: GameDao
    val games: Flow<List<Game>>
    private val _filterState = MutableStateFlow(GameFilter.ALL)
    val filteredGames: Flow<List<Game>>

    init {
        val database = Knowledge.getDB(application)
        gamesDao = database.gameDao()
        games = gamesDao.LoadAllGames()

        filteredGames = games.combine(_filterState) { games, filter ->
            when (filter) {
                GameFilter.ALL -> games
                GameFilter.WON -> games.filter { it.finalScore >= 50 }
                GameFilter.LOST -> games.filter { it.finalScore < 50 }
                GameFilter.HIGHEST_SCORE -> games.sortedByDescending { it.finalScore }
                GameFilter.LOWEST_SCORE -> games.sortedBy { it.finalScore }
                GameFilter.MOST_RECENT -> games.sortedByDescending { it.date }
                GameFilter.LEAST_RECENT -> games.sortedBy { it.date }
            }
        }
    }

    fun setFilter(filter: GameFilter) {
        _filterState.value = filter
    }
}
