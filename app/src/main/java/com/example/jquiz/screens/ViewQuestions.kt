import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row // NOUVEAU
import androidx.compose.foundation.layout.Spacer // NOUVEAU
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width // NOUVEAU
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jquiz.data.enums.QuestionKind
import com.example.jquiz.dto.MatchItem // NOUVEAU
import com.example.jquiz.viewmodels.ViewQuestionsViewModel
import kotlinx.serialization.json.Json

@Composable
fun ViewQuestionsScreen(vm: ViewQuestionsViewModel, gameId: Int) {
    val gameQuestions by vm.getGameQuestion(gameId).collectAsState(emptyList())
    val type by vm.getGameType(gameId).collectAsState(initial = QuestionKind.PRECISION)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(gameQuestions, key = { it.id }) { question ->
            GenericQuestionCard(
                isCorrect = question.isCorrect,
                questionContent = {
                    when (type) {
                        QuestionKind.PRECISION, QuestionKind.QCM -> {
                            val questionText = Json.decodeFromString<String>(question.questionData)
                            Text(questionText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        QuestionKind.MATCH -> {
                            val questionLabel = Json.decodeFromString<String>(question.questionData)
                            Text("Associate : $questionLabel", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        QuestionKind.DYNAMIC_SORT -> {
                            Text("Sort the list", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            val itemsToSort = Json.decodeFromString<List<String>>(question.questionData)
                            Column {
                                itemsToSort.forEachIndexed { index, item ->
                                    Text("${index + 1}. $item")
                                }
                            }
                        }
                    }
                },
                userAnswerContent = {
                    when (type) {
                        QuestionKind.PRECISION, QuestionKind.QCM -> {
                            val userAnswer = Json.decodeFromString<String>(question.userAnswer)
                            Text(userAnswer, color = if (question.isCorrect) Color.Unspecified else MaterialTheme.colorScheme.error)
                        }
                        QuestionKind.MATCH -> {
                            val userAnswers = Json.decodeFromString<List<MatchItem>>(question.userAnswer)
                            MatchItemsColumn(items = userAnswers)
                        }
                        QuestionKind.DYNAMIC_SORT -> {
                            val userSortedList = Json.decodeFromString<List<String>>(question.userAnswer)
                            Column {
                                userSortedList.forEachIndexed { index, item ->
                                    Text("${index + 1}. $item")
                                }
                            }
                        }
                    }
                },
                correctAnswerContent = {
                    when (type) {
                        QuestionKind.PRECISION, QuestionKind.QCM -> {
                            val correctAnswer = Json.decodeFromString<String>(question.correctAnswer)
                            Text(correctAnswer, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        // MODIFIÉ : Logique d'affichage pour les paires de MATCH
                        QuestionKind.MATCH -> {
                            val correctAnswers = Json.decodeFromString<List<MatchItem>>(question.correctAnswer)
                            MatchItemsColumn(items = correctAnswers)
                        }
                        QuestionKind.DYNAMIC_SORT -> {
                            val correctSortedList = Json.decodeFromString<List<String>>(question.correctAnswer)
                            Column {
                                correctSortedList.forEachIndexed { index, item ->
                                    Text("${index + 1}. $item")
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}


@Composable
private fun MatchItemsColumn(items: List<MatchItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        items.forEach { item ->
            Row {
                Text(item.name, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                Text("=", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.value, modifier = Modifier.weight(1f))
            }
        }
    }
}
