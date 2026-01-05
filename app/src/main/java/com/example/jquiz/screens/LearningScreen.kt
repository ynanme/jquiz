package com.example.jquiz.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jquiz.components.QuizDomainsChooser
import com.example.jquiz.dto.Info
import com.example.jquiz.viewmodels.LearningViewModel
import com.example.jquiz.viewmodels.StepL


@Composable
fun LearningScreen (model: LearningViewModel = viewModel()) {

    val isPremium by model.isPremiumFlow.collectAsState(initial = false)

    if (!isPremium) {
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("You need to be a premium user to access this feature")
        }
    } else {
        var step by model.step
        when (step) {
            StepL.HOME -> Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { model.next() }) { Text("Learn") }
            }
            StepL.DOMAINS_SELECTION -> QuizDomainsChooser(model.domainFields, { model.choseDomain(it) }, { model.isDomainChosen(it) }, { model.next() }) { model.previous() }
            StepL.LEARNING -> Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LearningCard(model.info.value, model.flipped.value, { model.flip() })
                Button(onClick = { model.nextInfo() }, modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(16.dp)) { Text("Next") }
                Button(onClick = { model.previous() }, modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(16.dp)) { Text("<- Back") }
            }
        }
    }

}


@Composable
fun LearningCard (info: Info, flipped: Boolean, flipper: () -> Unit, modifier: Modifier = Modifier) {

    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(500),
        label = "card-rotation"
    )

    Card (
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12 * density
            }
            .clickable { flipper() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        if (rotation <= 90f) {
            CardFront(info.title)
        } else {
            CardBack(info.infos)
        }
    }
}


@Composable
private fun CardFront (title: String) {
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}


@Composable
private fun CardBack (infos: Map<String, String>) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { rotationY = 180f }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        infos.forEach { (key, value) ->
            InfoRow(key, value)
        }
    }
}


@Composable
private fun InfoRow (label: String, value: String) {
    Row {
        Text(
            text = "$label : ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

