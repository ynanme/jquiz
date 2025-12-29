package com.example.jquiz.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.models.Planet
import com.example.jquiz.dto.Info
import com.example.jquiz.services.AccountManager
import com.example.jquiz.services.InfoGenerator
import kotlinx.coroutines.launch


enum class StepL {
    HOME,
    DOMAINS_SELECTION,
    LEARNING
}

class LearningViewModel (application: Application): AndroidViewModel(application) {

    private val db = Knowledge.getDB(application)

    private val chemicalElementsDao = db.chemicalElementsDao()
    private val countriesDao = db.countriesDao()
    private val footballTeamsDao = db.footballTeamsDao()
    private val historicEventsDao = db.historicEventsDao()
    private val planetsDao = db.planetsDao()

    val domainFields = listOf(
        Fields.COUNTRY to "GEOGRAPHY",
        Fields.CHEMICAL_ELEMENT to "CHEMISTRY",
        Fields.FOOTBALL_TEAM to "SPORTS",
        Fields.HISTORIC_EVENT to "HISTORY",
        Fields.PLANET to "ASTRONOMY"
    )

    private val accountManager = AccountManager(application.accountDatastore)

    val isPremiumFlow = accountManager.getIsPremiumFlow()
    val step = mutableStateOf(StepL.HOME)

    val chosenDomains = mutableStateListOf<Fields>()

    val info = mutableStateOf(Info(title = "Info", infos = mapOf()))

    val flipped = mutableStateOf(false)


    fun next () {
        if (step.value == StepL.HOME) step.value = StepL.DOMAINS_SELECTION
        if (step.value == StepL.DOMAINS_SELECTION && chosenDomains.isNotEmpty()) {
            step.value = StepL.LEARNING
            generate()
        }
    }

    fun previous () {
        if (step.value == StepL.DOMAINS_SELECTION) step.value = StepL.HOME
        if (step.value == StepL.LEARNING) step.value = StepL.DOMAINS_SELECTION
    }


    fun choseDomain (domain: Fields) {
        if (domain in chosenDomains) chosenDomains.remove(domain)
        else chosenDomains.add(domain)
    }

    fun isDomainChosen (domain: Fields): Boolean {
        return domain in chosenDomains
    }

    fun flip () {
        flipped.value = !flipped.value
    }

    fun nextInfo () {
        generate()
        flipped.value = false
    }


    private fun generate() {
        viewModelScope.launch {
            val domain = chosenDomains.random()

            info.value = when (domain) {
                Fields.CHEMICAL_ELEMENT -> {
                    val generator = InfoGenerator<ChemicalElement>()
                    generator.generate(chemicalElementsDao)
                }

                Fields.PLANET -> {
                    val generator = InfoGenerator<Planet>()
                    generator.generate(planetsDao)
                }

                Fields.HISTORIC_EVENT -> {
                    val generator = InfoGenerator<HistoricEvent>()
                    generator.generate(historicEventsDao)
                }

                Fields.FOOTBALL_TEAM -> {
                    val generator = InfoGenerator<FootballTeam>()
                    generator.generate(footballTeamsDao)
                }

                Fields.COUNTRY -> {
                    val generator = InfoGenerator<Country>()
                    generator.generate(countriesDao)
                }
            }

        }
    }

}