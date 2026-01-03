package com.example.jquiz.services

import android.content.Context
import com.example.jquiz.R

object Translator {

    private val labelMap = mapOf(
        "atomicNumber" to R.string.atomicNumber,
        "atomicMass" to R.string.atomicMass,
        "yearOfFoundation"   to R.string.yearOfFoundation,
        "c1Count" to R.string.c1Count,
        "euCount" to R.string.euCount,
    )

    fun getLabelOrNull(context: Context, code: String?): String? {
        val resId = labelMap[code] ?: return null
        return context.getString(resId)
    }
}
