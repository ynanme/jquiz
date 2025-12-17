package com.example.jquiz.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields


open class QuizViewModel (application: Application, protected val domains: List<Fields>, protected val level: Difficulty): AndroidViewModel(application) {

    private val db = Knowledge.getDB(application)

    private val chemicalElementsDao = db.chemicalElementsDao()
    private val countriesDao = db.countriesDao()
    private val footballTeamsDao = db.footballTeamsDao()
    private val historicEventsDao = db.historicEventsDao()
    private val planetsDao = db.planetsDao()

}



class QuizViewModelFactory(
    private val application: Application,
    private val domains: List<Fields>,
    private val level: Difficulty
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {

            modelClass.isAssignableFrom(QCMViewModel::class.java) ->
                QCMViewModel(application, domains, level) as T

            else -> throw IllegalArgumentException("Unknown ViewModel")

        }
    }
}
