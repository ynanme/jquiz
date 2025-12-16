package com.example.jquiz.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.jquiz.components.QuizDomainsChooser
import com.example.jquiz.components.QuizTypeChooser
import com.example.jquiz.components.Quizer
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind



enum class Step {
    TYPE_CHOICE,
    DOMAINS_CHOICE,
    QUIZ
}



@Composable
fun QuizScreen () {

    var step by remember { mutableStateOf(Step.TYPE_CHOICE) }

    var chosenType by remember { mutableStateOf<QuestionKind?>(null) }
    var chosenLevel by remember { mutableStateOf(Difficulty.MEDIUM) }
    val chosenDomains = remember { mutableStateListOf<Fields>() }

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

    when (step) {

        Step.TYPE_CHOICE -> QuizTypeChooser(
            typeFields,
            levelFields,
            { chosenType = it; step = Step.DOMAINS_CHOICE },
            { chosenLevel = it },
            chosenLevel
        )

        Step.DOMAINS_CHOICE -> QuizDomainsChooser(
            domainFields,
            { if (it in chosenDomains) chosenDomains.remove(it) else chosenDomains.add(it) },
            { it in chosenDomains },
            { if (chosenDomains.isNotEmpty()) step = Step.QUIZ },
            { step = Step.TYPE_CHOICE }
        )

        Step.QUIZ -> Quizer(chosenType, chosenLevel, chosenDomains) { step = Step.DOMAINS_CHOICE }

    }

}

