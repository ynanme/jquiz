package com.example.jquiz.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable


sealed interface Screen: NavKey {

    val icon: ImageVector

    @Serializable
    data object  Quiz: Screen {
        override val icon = Icons.Filled.Quiz
    }

    @Serializable
    data object Learning: Screen {
        override val icon = Icons.Filled.Book
    }

    @Serializable
    data object History: Screen {
        override val icon = Icons.Filled.History
    }

}


@Composable
fun MainScreen () {

    val backStack = rememberNavBackStack(Screen.Quiz)

    Scaffold (
        topBar = { TopBar("${backStack.last()}") },
        bottomBar = { BottomBar(backStack, listOf(Screen.Quiz, Screen.Learning, Screen.History)) }
    ) { paddingValues ->
        NavDisplay(
            backStack=backStack,
            modifier = Modifier.padding(paddingValues),
            entryProvider = entryProvider {
                entry<Screen.Quiz> { QuizScreen() }
                entry<Screen.Learning> { LearningScreen() }
                entry<Screen.History> { HistoryScreen() }
            }
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (currentScreen: String) {
    CenterAlignedTopAppBar(
        title = { Text(currentScreen) }
    )
}


@Composable
fun BottomBar (backStack: NavBackStack<NavKey>, screens: List<Screen>) {
    NavigationBar {
        val current = backStack.last()
        screens.forEach {
            NavigationBarItem(
                selected = it==current,
                onClick = { backStack += it },
                icon = { Icon(it.icon, "$it") }
            )
        }
    }
}

