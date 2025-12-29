package com.example.jquiz.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jquiz.data.daos.ChemicalElementsDao
import com.example.jquiz.data.daos.CountriesDao
import com.example.jquiz.data.daos.FootballTeamsDao
import com.example.jquiz.data.daos.GameDao
import com.example.jquiz.data.daos.GameQuestionsDao
import com.example.jquiz.data.daos.HistoricEventsDao
import com.example.jquiz.data.daos.PlanetsDao
import com.example.jquiz.data.daos.QuestionTypeDao
import com.example.jquiz.data.models.Planet
import com.example.jquiz.data.models.ChemicalElement
import com.example.jquiz.data.models.FootballTeam
import com.example.jquiz.data.models.Country
import com.example.jquiz.data.models.Game
import com.example.jquiz.data.models.GameQuestions
import com.example.jquiz.data.models.HistoricEvent
import com.example.jquiz.data.models.QuestionType
import com.example.jquiz.data.seed.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@Database(entities = [ChemicalElement::class, Country::class, FootballTeam::class, HistoricEvent::class, Planet::class, QuestionType::class, Game::class, GameQuestions::class], version = 1)
abstract  class Knowledge : RoomDatabase() {


    abstract  fun chemicalElementsDao (): ChemicalElementsDao
    abstract  fun countriesDao (): CountriesDao
    abstract  fun footballTeamsDao (): FootballTeamsDao
    abstract  fun historicEventsDao (): HistoricEventsDao
    abstract  fun planetsDao (): PlanetsDao
    abstract  fun questionTypeDao (): QuestionTypeDao
    abstract fun gameDao() : GameDao
    abstract fun gameQuestionsDao() : GameQuestionsDao






    companion object {

        private val applicationScope = CoroutineScope(SupervisorJob())
        @Volatile
        private var instance: Knowledge? = null

        fun getDB (context: Context): Knowledge {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    Knowledge::class.java,
                    "knowledge.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            applicationScope.launch {
                                val dbInstance = instance ?: return@launch
                                DatabaseSeeder(context, dbInstance).seedIfNeeded()
                            }
                        }
                    })
                    .fallbackToDestructiveMigration(true)
                    .build()

            }
            return instance !!
        }

    }

}