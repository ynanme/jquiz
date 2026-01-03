package com.example.jquiz.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.components.QuizCooldown
import com.example.jquiz.components.QuizDomainsChooser
import com.example.jquiz.components.QuizTypeChooser
import com.example.jquiz.components.Quizer

import com.example.jquiz.viewmodels.QuizSelectionViewModel
import com.example.jquiz.viewmodels.Step


@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun QuizScreen (model: QuizSelectionViewModel = viewModel()) {

    var step by model.step
    val remainingTime by model.remainingTime
    val shouldAskPermission by model.shouldAskNotificationPermission
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        model.onNotificationPermissionResult(granted)
    }

    LaunchedEffect(shouldAskPermission) {
        if (shouldAskPermission) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }



    when (step) {

        Step.TYPE_CHOICE -> QuizTypeChooser(
            model.typeFields,
            model.levelFields,
            { model.choseType(it) },
            { model.choseLevel(it) },
            model.chosenLevel.value
        )

        Step.DOMAINS_CHOICE -> QuizDomainsChooser(
            model.domainFields,
            { model.choseDomain((it)) },
            { model.isDomainChosen(it) },
            { model.nextAfterDomains() },
            { model.previous() }
        )

        Step.QUIZ -> {
            Quizer(
                model.chosenType.value,
                model.chosenLevel.value,
                model.chosenDomains
            ) { model.previous() }
        }

        Step.COOLDOWN -> {
            QuizCooldown(remainingTime)
        }

    }

}

