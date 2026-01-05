package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.data.models.GameQuestions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ViewQuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Knowledge.getDB(application)
    private val gameQuestionsDao = db.gameQuestionsDao()
    private val gameDao = db.gameDao()

    fun getGameQuestion(gameId: Int): Flow<List<GameQuestions>> {
        return gameQuestionsDao.LoadGameQuestionsByGameId(gameId)
    }

    fun getGameType(gameId: Int): Flow<QuestionKind> = flow {
        emit(gameDao.getQuizKind(gameId))
    }
}