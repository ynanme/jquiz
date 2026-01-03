import android.R.attr.type
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.jquiz.viewmodels.ViewQuestionsViewModel
import kotlinx.serialization.json.Json
import kotlin.collections.emptyList
import kotlin.getValue


@Composable
fun ViewQuestionsScreen(vm: ViewQuestionsViewModel, gameId: Int) {
    val gameQuestions by vm.getGameQuestion(gameId).collectAsState(emptyList())
    val type by vm.getGameType(gameId).collectAsState(initial =  QuestionKind.PRECISION)


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        QuestionKind.MATCH, QuestionKind.DYNAMIC_SORT -> {
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
                        QuestionKind.MATCH, QuestionKind.DYNAMIC_SORT -> {
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
                        QuestionKind.MATCH, QuestionKind.DYNAMIC_SORT -> {
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

