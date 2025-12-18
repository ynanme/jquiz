package com.example.jquiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jquiz.dto.QuizResult


@Composable
fun QuizRecapBox (
    result: QuizResult,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Quiz completed 🎉",
                style = MaterialTheme.typography.headlineSmall
            )

            ScoreBadge(result.score, result.total)

            Text("Type: ${result.type}")
            Text("Difficulty: ${result.difficulty}")
            Text("Domains: ${result.domains.joinToString()}")

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(onClick = onRetry) {
                    Text("Retry")
                }
                OutlinedButton(onClick = onBack) {
                    Text("Back")
                }
            }
        }
    }
}


@Composable
fun ScoreBadge(score: Int, total: Int) {
    val ratio = score.toFloat() / total

    val color = when {
        ratio >= 0.8f -> Color(0xFF4CAF50)
        ratio >= 0.5f -> Color(0xFFFFC107)
        else -> Color(0xFFF44336)
    }

    Box(
        modifier = Modifier
            .size(120.dp)
            .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$score / $total",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}


