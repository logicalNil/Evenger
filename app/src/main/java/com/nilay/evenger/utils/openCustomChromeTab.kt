package com.nilay.evenger.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors

fun Context.openCustomChromeTab(link: String) = this.run {
    val defaultColors = CustomTabColorSchemeParams.Builder().setToolbarColor(
        MaterialColors.getColor(
            this, androidx.appcompat.R.attr.colorAccent, Color.RED
        )
    ).build()
    try {
        val customTabIntent =
            CustomTabsIntent.Builder().setDefaultColorSchemeParams(defaultColors).build()
        customTabIntent.intent.`package` = "com.android.chrome"
        customTabIntent.launchUrl(this, Uri.parse(link))
    } catch (e: Exception) {
        Toast.makeText(this, "Invalid Link", Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.openLinkToDefaultApp(link: String) = this.run {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    } catch (_: Exception) {
        requireContext().openCustomChromeTab(link)
    }
}