package com.example.jquiz.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.components.QuizCooldown
import com.example.jquiz.components.QuizDomainsChooser
import com.example.jquiz.components.QuizTypeChooser
import com.example.jquiz.components.Quizer
import com.example.jquiz.data.enums.Difficulty
import com.example.jquiz.data.enums.Fields
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.viewmodels.QuizSelectionViewModel
import com.example.jquiz.viewmodels.Step


@Composable
fun QuizScreen (model: QuizSelectionViewModel = viewModel()) {

    var step by model.step

    var chosenType by model.chosenType
    var chosenLevel by model.chosenLevel
    val chosenDomains = model.chosenDomains


    when (step) {

        Step.TYPE_CHOICE -> QuizTypeChooser(
            model.typeFields,
            model.levelFields,
            { model.choseType(it) },
            { model.choseLevel(it) },
            chosenLevel
        )

        Step.DOMAINS_CHOICE -> QuizDomainsChooser(
            model.domainFields,
            { model.choseDomain((it)) },
            { model.isDomainChosen(it) },
            { model.nextAfterDomains() },
            { model.previous() }
        )

        Step.QUIZ -> {
            val canQuiz by model.canQuiz
            val remainingTime by model.remainingTime
            if (canQuiz) {
                Quizer(chosenType, chosenLevel, chosenDomains) { model.previous() }
                LaunchedEffect(Unit) { model.registerQuizTimestamp() }
            } else QuizCooldown(remainingTime)
        }

    }

}

