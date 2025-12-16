package com.example.jquiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jquiz.data.daos.ChemicalElementsDao
import com.example.jquiz.data.daos.CountriesDao
import com.example.jquiz.data.daos.FootballTeamsDao
import com.example.jquiz.data.daos.HistoricEventsDao
import com.example.jquiz.data.daos.PlanetsDao
import com.example.jquiz.data.models.Planet
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.HistoricEvent


@Database(entities = [ChemicalElement::class, Country::class, FootballTeam::class, HistoricEvent::class, Planet::class], version = 1)
abstract  class Knowledge : RoomDatabase() {


    abstract  fun chemicalElementsDao (): ChemicalElementsDao
    abstract  fun countriesDao (): CountriesDao
    abstract  fun footballTeamsDao (): FootballTeamsDao
    abstract  fun historicEventsDao (): HistoricEventsDao
    abstract  fun planetsDao (): PlanetsDao


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