package com.example.jquiz.data.seed

import android.content.Context
import androidx.room.withTransaction
import com.example.jquiz.data.Knowledge
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.models.Planet
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

class DatabaseSeeder(
    private val context: Context,
    private val db: Knowledge
) {

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun seedIfNeeded() {
        db.withTransaction {
            seedPlanets()
            seedChemicalElements()
            seedCountries()
            seedFootballTeams()
            seedHistoricEvents()
        }
    }

    private suspend fun seedPlanets() {
        if (db.planetsDao().count() > 0) return

        val rawJson = context.readAsset("seed/planets.json")
        val planets = json.decodeFromString(
            ListSerializer(Planet.serializer()),
            rawJson
        )

        db.planetsDao().insertAll(planets)
    }

    private suspend fun seedChemicalElements() {
        if(db.chemicalElementsDao().count() > 0) return

        val rawJson = context.readAsset("seed/chemicalElements.json")
        val elements = json.decodeFromString(
            ListSerializer(ChemicalElement.serializer()),
            rawJson
        )

        db.chemicalElementsDao().insertAll(elements)
    }

    private suspend fun seedCountries() {
        if(db.countriesDao().count() > 0) return

        val rawJson = context.readAsset("seed/countries.json")
        val countries = json.decodeFromString(
            ListSerializer(Country.serializer()),
            rawJson
        )

        db.countriesDao().insertAll(countries)
    }

    private suspend fun seedFootballTeams() {
        if(db.footballTeamsDao().count() > 0) return

        val rawJson = context.readAsset("seed/footballTeams.json")
        val teams = json.decodeFromString(
            ListSerializer(FootballTeam.serializer()),
            rawJson
        )

        db.footballTeamsDao().insertAll(teams)
    }

    private suspend fun seedHistoricEvents() {
        if(db.historicEventsDao().count() > 0) return

        val rawJson = context.readAsset("seed/historicEvents.json")
        val events = json.decodeFromString(
            ListSerializer(HistoricEvent.serializer()),
            rawJson
        )

        db.historicEventsDao().insertAll(events)
    }


}
