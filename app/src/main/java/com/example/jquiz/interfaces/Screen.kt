package com.example.jquiz.interfaces

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
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

    @Serializable
    data object Settings: Screen {
        override val icon = Icons.Filled.Settings
    }


}