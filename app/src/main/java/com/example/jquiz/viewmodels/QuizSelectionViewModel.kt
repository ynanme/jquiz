package com.example.jquiz.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.services.AccountManager
import com.example.jquiz.services.NotificationsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



enum class Step {
    TYPE_CHOICE,
    DOMAINS_CHOICE,
    QUIZ,
    COOLDOWN
}

class QuizSelectionViewModel (application: Application): AndroidViewModel(application) {

    private val accountManager = AccountManager(application.accountDatastore)

    val canQuiz = mutableStateOf(false)
    val remainingTime = mutableLongStateOf(0L)
    private var cooldownJob: Job? = null
    val notificationsManager = NotificationsManager(
        notifications = application.notificationsDataStore
    )
    val shouldAskNotificationPermission = mutableStateOf(false)

    var quizSessionId = mutableLongStateOf(0)
        private set




    val typeFields = listOf(
        QuestionKind.QCM to "QCM",
        QuestionKind.PRECISION to "PRECISION",
        QuestionKind.MATCH to "MATCH",
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
        else if (step.value == Step.DOMAINS_CHOICE) {
            tryStartQuiz()
        }
    }

    fun previous () {
        if (step.value == Step.DOMAINS_CHOICE) {
            step.value = Step.TYPE_CHOICE
            chosenType.value = null
            chosenDomains.clear()
        }
        else if (step.value == Step.QUIZ) {
            step.value = Step.DOMAINS_CHOICE
            chosenDomains.clear()
        }
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


    fun refreshQuizState() {
        viewModelScope.launch {
            canQuiz.value = accountManager.canQuiz()
            remainingTime.longValue = accountManager.remainingTime()
        }
    }

    fun tryStartQuiz() {
        viewModelScope.launch {
            if (accountManager.canQuiz()) {
                accountManager.registerNewQuiz()
                canQuiz.value = true
                quizSessionId.longValue++
                step.value = Step.QUIZ
                return@launch
            }
            canQuiz.value = false
            step.value = Step.COOLDOWN
            val hasPermission =
                notificationsManager.hasNotificationPermission()

            shouldAskNotificationPermission.value = !hasPermission

            notificationsManager.scheduleCooldownFinished(
                accountManager.remainingTime(),
                application.applicationContext
            )

            startCooldown()
        }
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            notificationsManager.setPermissions(granted)
            shouldAskNotificationPermission.value = false
        }
    }


    private fun startCooldown() {
        cooldownJob?.cancel()
        cooldownJob = viewModelScope.launch {
            while (true) {
                val time = accountManager.remainingTime()
                remainingTime.longValue = time
                if (time <= 0L) {
                    canQuiz.value = true
                    step.value = Step.DOMAINS_CHOICE
                    break
                }
                delay(1_000)
            }
        }
    }


    init {
        refreshQuizState()
    }

}