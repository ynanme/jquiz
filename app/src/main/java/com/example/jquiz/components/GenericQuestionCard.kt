import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


/**
 * Un composable de carte générique pour afficher n'importe quel type de question.
 *
 * @param isCorrect Indique si la réponse de l'utilisateur était correcte.
 * @param questionContent Le slot pour afficher le contenu de la question (texte, liste, etc.).
 * @param userAnswerContent Le slot pour afficher la réponse de l'utilisateur.
 * @param correctAnswerContent Le slot (optionnel) pour afficher la bonne réponse si l'utilisateur s'est trompé.
 */
@Composable
fun GenericQuestionCard(
    isCorrect: Boolean,
    questionContent: @Composable () -> Unit,
    userAnswerContent: @Composable () -> Unit,
    correctAnswerContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCorrect) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Question :",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            questionContent()

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Your answer :",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    userAnswerContent()
                }
                Icon(
                    imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = if (isCorrect) "Right Answer" else "Wrong Answer",
                    tint = if (isCorrect) Color(0xFF008000) else MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                )
            }

            if (!isCorrect && correctAnswerContent != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Right Answer :",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                correctAnswerContent()
            }
        }
    }
}
