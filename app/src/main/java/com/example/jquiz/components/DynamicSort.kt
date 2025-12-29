package com.example.jquiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.jquiz.dto.DynamicSortItem
import com.example.jquiz.viewmodels.DynamicSortViewModel
import kotlin.math.roundToInt
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize



@Composable
fun DynamicSort(model: DynamicSortViewModel, onQuit: () -> Unit) {

    if (!model.isQuizOver()) DynamicSortQuestionBox(model)
    else QuizRecapBox(model.getQuizResult(), { model.reset() }) { onQuit(); model.reset() }

}


@Composable
fun DynamicSortQuestionBox (model: DynamicSortViewModel) {

    val itemToSort = model.itemToSort.value

    var draggedItem by remember { mutableStateOf<DynamicSortItem?>(null) }
    var dragPosition by remember { mutableStateOf(Offset.Zero) }
    val slotBounds = remember { mutableStateMapOf<Int, Rect>() }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var previewRect by remember { mutableStateOf<Rect?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(text = model.question.value?.label ?: "Sort", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

        itemToSort?.let { ItemToSortCard(
            itemToSort,
            { draggedItem = it },
            { screenPosition, dragAmount -> dragPosition = screenPosition; dragOffset += dragAmount },
            {
                val preview = previewRect
                val index = slotBounds.entries.find { (_, rect) -> preview != null && preview.overlaps(rect) }?.key
                if (index != -1 && index != null && draggedItem != null) {
                    model.trySort(index)
                }
                draggedItem = null
                dragPosition = Offset.Zero
            }
        ) } ?: run {
            Button(onClick = { model.endQuiz() }) { Text("Recap") }
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        SortedItems(model.sortedItems, slotBounds, draggedItem != null) { model.itemHasBeenInsertedCorrectly(it) }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        draggedItem?.let {
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            dragPosition.x.roundToInt(),
                            dragPosition.y.roundToInt()
                        )
                    }
                    .onGloballyPositioned { box ->
                        previewRect = box.boundsInWindow()
                    }
            ) {
                ItemCard(it, false, null)
            }
        }
    }

}


@Composable
fun SortedItems(items: List<DynamicSortItem?>, slotBounds: SnapshotStateMap<Int, Rect>, showSlots: Boolean, evaluator: (DynamicSortItem) -> Boolean?) {
    Row {
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outlineVariant)
        )

        Spacer(Modifier.width(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEachIndexed { index, item -> item?.let {
                if (showSlots) DropSlot(index) { i, rect -> slotBounds[i] = rect }
                ItemCard(item, true, evaluator(item))
            } }
            if (showSlots) DropSlot(items.size) { i, rect -> slotBounds[i] = rect }
        }
    }
}


@Composable
fun ItemCard(item: DynamicSortItem, showValue: Boolean, hasBeenInsertedCorrectly: Boolean?) {
    val color = when (hasBeenInsertedCorrectly) {
        true -> Color(0xFF66BB6A)
        false -> Color(0xFFEF5350)
        else -> MaterialTheme.colorScheme.surface
    }
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = color)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(item.label, fontWeight = FontWeight.Medium)
            if (showValue) {
                Spacer(modifier = Modifier.weight(1f))
                Text("${item.value}", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@Composable
fun ItemToSortCard (item: DynamicSortItem, onDragStart: () -> Unit, onDrag: (Offset, Offset) -> Unit, onDragEnd: () -> Unit) {
    Box (
        modifier = Modifier
            .pointerInput(item) {
                detectDragGestures(
                    onDragStart = { onDragStart() },
                    onDrag = { change, amount ->
                        change.consume()
                        onDrag(change.position, amount)
                    },
                    onDragEnd = { onDragEnd() }
                )
            }
    ) {
        ItemCard(item, false, null)
    }
}


@Composable
fun DropSlot (index: Int, onBounds: (Int, Rect) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .onGloballyPositioned {
                onBounds(index, it.boundsInWindow())
            }
    )
}

