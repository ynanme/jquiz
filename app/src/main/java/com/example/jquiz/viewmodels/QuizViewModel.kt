package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.interfaces.DataSource


abstract class QuizViewModel (application: Application, protected val domains: List<Fields>, protected val level: Difficulty): AndroidViewModel(application) {

    protected val db = Knowledge.getDB(application)

    protected val chemicalElementsDao = db.chemicalElementsDao()
    protected val countriesDao = db.countriesDao()
    protected val footballTeamsDao = db.footballTeamsDao()
    protected val historicEventsDao = db.historicEventsDao()
    protected val planetsDao = db.planetsDao()

    public var quizIsOver = mutableStateOf(false)

    protected var correctAnswersCount = 0

    protected val questionsCount = 5

    public abstract fun reset();

    fun isQuizOver (): Boolean {
        return quizIsOver.value
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

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
