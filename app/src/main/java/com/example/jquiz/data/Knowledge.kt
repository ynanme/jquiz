package com.example.jquiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jquiz.data.daos.ChemicalElementsDAO
import com.example.jquiz.data.daos.CountriesDAO
import com.example.jquiz.data.daos.FootballTeamsDAO
import com.example.jquiz.data.daos.HistoricEventsDAO
import com.example.jquiz.data.daos.PlanetsDAO
import com.example.jquiz.data.models.Planet
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.HistoricEvent


@Database(entities = [ChemicalElement::class, Country::class, FootballTeam::class, HistoricEvent::class, Planet::class], version = 1)
abstract  class Knowledge : RoomDatabase() {


    abstract  fun chemicalElementsDAO (): ChemicalElementsDAO
    abstract  fun countriesDAO (): CountriesDAO
    abstract  fun footballTeamsDAO (): FootballTeamsDAO
    abstract  fun historicEventsDAO (): HistoricEventsDAO
    abstract  fun planetsDAO (): PlanetsDAO


    companion object {

        @Volatile
        private var instance: Knowledge? = null

        fun getDB (context: Context): Knowledge {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, Knowledge::class.java, "knowledge.db")
                    .fallbackToDestructiveMigration(true)
                    .build()
            }
            return instance !!
        }

    }

}