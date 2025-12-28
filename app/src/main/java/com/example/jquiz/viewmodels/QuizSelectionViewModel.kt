package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.services.AccountManager
import kotlinx.coroutines.launch



enum class Step {
    TYPE_CHOICE,
    DOMAINS_CHOICE,
    QUIZ
}


class QuizSelectionViewModel (application: Application): AndroidViewModel(application) {

    private val accountManager = AccountManager(application.dataStore)

    val canQuiz = mutableStateOf(true)
    val remainingTime = mutableLongStateOf(0L)

    val typeFields = listOf(
        QuestionKind.QCM to "QCM",
        QuestionKind.PRECISION to "PRECISION",
        QuestionKind.SORT to "SORT",
        QuestionKind.DYNAMIC_SORT to "DYNAMIC SORT"
    )

    val levelFields = listOf(
        Difficulty.EASY to "EASY",
        Difficulty.MEDIUM to "MEDIUM",
        Difficulty.HARD to "HARD"
    )

    val domainFields = listOf(
        Fields.COUNTRY to "GEOGRAPHY",
        Fields.CHEMICAL_ELEMENT to "CHEMISTRY",
        Fields.FOOTBALL_TEAM to "SPORTS",
        Fields.HISTORIC_EVENT to "HISTORY",
        Fields.PLANET to "ASTRONOMY",
        )

    var step = mutableStateOf(Step.TYPE_CHOICE)
    var chosenType = mutableStateOf<QuestionKind?>(null)
    var chosenLevel = mutableStateOf(Difficulty.MEDIUM)
    val chosenDomains = mutableStateListOf<Fields>()


    fun next () {
        if (step.value == Step.TYPE_CHOICE) step.value = Step.DOMAINS_CHOICE
        else if (step.value == Step.DOMAINS_CHOICE) step.value = Step.QUIZ
    }

    fun previous () {
        if (step.value == Step.DOMAINS_CHOICE) step.value = Step.TYPE_CHOICE
        else if (step.value == Step.QUIZ) step.value = Step.DOMAINS_CHOICE
    }

    fun nextAfterDomains () {
        if (chosenDomains.isNotEmpty()) next()
    }

    fun choseLevel (level: Difficulty) {
        chosenLevel.value = level
    }

    fun choseType (type: QuestionKind) {
        chosenType.value = type
        next()
    }

    fun choseDomain (domain: Fields) {
        if (domain in chosenDomains) chosenDomains.remove(domain)
        else chosenDomains.add(domain)
    }

    fun isDomainChosen (domain: Fields): Boolean {
        return domain in chosenDomains
    }


    suspend fun canQuiz (): Boolean {
        return accountManager.canQuiz()
    }

    fun registerQuizTimestamp () {
        viewModelScope.launch {
            accountManager.registerNewQuiz()
            refreshQuizState()
        }
    }

    suspend fun remainingTime (): Long {
        return accountManager.remainingTime()
    }

    fun refreshQuizState() {
        viewModelScope.launch {
            canQuiz.value = accountManager.canQuiz()
            remainingTime.longValue = accountManager.remainingTime()
        }
    }

    init {
        refreshQuizState()
    }

}