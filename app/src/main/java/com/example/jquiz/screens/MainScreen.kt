package com.example.jquiz.screens

import ViewQuestionsScreen
import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.jquiz.interfaces.Screen
import com.example.jquiz.viewmodels.HistoryViewModel
import com.example.jquiz.viewmodels.ViewQuestionsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun MainScreen () {

    val context = LocalContext.current

    val historyVm = androidx.compose.runtime.remember {
        HistoryViewModel(context.applicationContext as Application)
    }

    val viewQuestionsVm = androidx.compose.runtime.remember {
        ViewQuestionsViewModel(context.applicationContext as Application)
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStack = rememberNavBackStack(Screen.Quiz)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { ModalDrawerSheet { DrawerContent(backStack, drawerState, scope) } }
    ) {
        Scaffold (
            topBar = { TopBar("${backStack.last()}") { scopeLauncher(drawerState, scope) } },
            bottomBar = { BottomBar(backStack, listOf(Screen.Quiz, Screen.Learning, Screen.History)) }
        ) { paddingValues ->
            NavDisplay(
                backStack=backStack,
                modifier = Modifier.padding(paddingValues),
                entryProvider = entryProvider {
                    entry<Screen.Quiz> { QuizScreen() }
                    entry<Screen.Learning> { LearningScreen() }
                    entry<Screen.History> { HistoryScreen(historyVm, { backStack+=Screen.ViewQuestions(it) }) }
                    entry<Screen.Settings> {SettingsScreen()}
                    entry<Screen.ViewQuestions> {ViewQuestionsScreen(viewQuestionsVm ,it.gameId) }
                    entry<Screen.Account> { AccountScreen() }
                    entry<Screen.Notifications> { NotificationsScreen() }
                }
            )
        }
    }

}


@Composable
fun DrawerContent (backStack: NavBackStack<NavKey>, drawerState: DrawerState, scope: CoroutineScope) {
    Column {
        NavigationDrawerItem(
            selected = false,
            label = { Text("Account") },
            onClick = { scope.launch { drawerState.close(); backStack += Screen.Account } }
        )
        NavigationDrawerItem(selected = false, label = { Text("Preferences") }, onClick = {})
        NavigationDrawerItem(
            selected = false,
            label = { Text("Notifications") },
            onClick = { scope.launch { drawerState.close(); backStack += Screen.Notifications } }
        )
        NavigationDrawerItem(selected = false, label = { Text("About") }, onClick = {})

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (currentScreen: String, launchScope: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(currentScreen) },
        navigationIcon = { IconButton(onClick = launchScope) { Icon(Icons.Default.Menu, contentDescription = "Menu") } }
    )
}


@Composable
fun BottomBar (backStack: NavBackStack<NavKey>, screens: List<Screen>) {
    NavigationBar {
        val current = backStack.last()
        screens.forEach {
            NavigationBarItem(
                selected = it==current,
                onClick = {
                    if(current !in listOf(Screen.Quiz, Screen.Learning, Screen.History)) {
                        backStack+=it
                    }
                    else if(it != current) {
                        backStack.remove(current)
                        backStack+=it
                    }
                },
                icon = { Icon(it.icon, "$it") }
            )
        }
    }
}



fun scopeLauncher (drawerState: DrawerState, scope: CoroutineScope) {
    scope.launch {
        if (drawerState.isClosed) {
            drawerState.open()
        } else {
            drawerState.close()
        }
    }
}

