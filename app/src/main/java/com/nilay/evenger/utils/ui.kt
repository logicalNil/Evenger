package com.nilay.evenger.utils

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

fun String.getAndSetHint(type: String): Int = when (this) {
    type -> 0
    else -> this.toInt()
}


fun Context.isDark() =
    resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun setAppTheme(type: Int) {
    AppCompatDelegate.setDefaultNightMode(type)
}