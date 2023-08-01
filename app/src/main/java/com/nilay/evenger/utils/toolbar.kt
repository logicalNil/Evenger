package com.nilay.evenger.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.nilay.evenger.R
import com.nilay.evenger.databinding.LayoutToolbarBinding

data class ToolbarData(
    @StringRes
    val title: Int = R.string.app_name,
    val titleString: String? = null,
    @DrawableRes
    val icon: Int = R.drawable.round_arrow_back_24,
    val action: () -> Unit,
)

fun LayoutToolbarBinding.set(toolbarData: ToolbarData) = this.apply {
    if (toolbarData.titleString != null) textViewTitle.text = toolbarData.titleString
    else textViewTitle.text = root.context.getText(toolbarData.title)
    materialButtonClose.apply {
        setIconResource(toolbarData.icon)
        setOnClickListener { toolbarData.action() }
    }
}