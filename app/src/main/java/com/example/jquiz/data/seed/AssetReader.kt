package com.example.jquiz.data.seed

import android.content.Context

fun Context.readAsset(path: String): String =
    assets.open(path).bufferedReader().use { it.readText() }
