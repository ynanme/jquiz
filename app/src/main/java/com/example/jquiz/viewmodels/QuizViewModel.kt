package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.data.models.Game
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


abstract class QuizViewModel (application: Application, protected val domains: List<Fields>, protected val level: Difficulty): AndroidViewModel(application) {

    protected val db = Knowledge.getDB(application)
    protected val chemicalElementsDao = db.chemicalElementsDao()
    protected val countriesDao = db.countriesDao()
    protected val footballTeamsDao = db.footballTeamsDao()
    protected val historicEventsDao = db.historicEventsDao()
    protected val planetsDao = db.planetsDao()
    protected val gameDao = db.gameDao()
    protected val gameQuestionsDao = db.gameQuestionsDao()


    public var quizIsOver = mutableStateOf(false)
    protected lateinit var game: Game
    protected var correctAnswersCount = 0
    protected var questionsCount  =
        when (level) {
            Difficulty.EASY -> 4
            Difficulty.MEDIUM -> 6
            Difficulty.HARD -> 8
        }

    protected var currentGameId: Long? = null

    public abstract fun reset();

    protected abstract fun generateQuestion();


    fun isQuizOver (): Boolean {
        return quizIsOver.value
    }



    fun updateGame(endedGame: Boolean = true) {
        game.let { currentGame ->
            currentGame.finalScore = ((correctAnswersCount.toFloat() / questionsCount.toFloat()) * 100).toInt()
            currentGame.endedGame = endedGame
            viewModelScope.launch {
                gameDao.update(currentGame)
            }
        }
    }

    abstract fun getQuestionKind() : QuestionKind


    suspend fun launchGame() {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val current = LocalDateTime.now().format(formatter)
        val newGame = Game(finalScore = 0, date = current, difficulty = level.value.average().toInt(), endedGame = false, type = getQuestionKind())

        val generatedId = gameDao.AddGame(newGame)
        currentGameId = generatedId

        game = newGame.copy(id = generatedId.toInt())
    }

    init{
        viewModelScope.launch {
            launchGame()
            generateQuestion()
        }
    }
}



class QuizViewModelFactory(
    private val application: Application,
    private val domains: List<Fields>,
    private val level: Difficulty
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {

            modelClass.isAssignableFrom(PrecisionViewModel::class.java) ->
                PrecisionViewModel(application, domains, level) as T

            modelClass.isAssignableFrom(QCMViewModel::class.java) ->
                QCMViewModel(application, domains, level) as T

            modelClass.isAssignableFrom(DynamicSortViewModel::class.java) ->
                DynamicSortViewModel(application, domains, level) as T

            modelClass.isAssignableFrom(MatchViewModel::class.java) ->
                MatchViewModel(application, domains, level) as T

            else -> throw IllegalArgumentException("Unknown ViewModel")

        }
    }
}
