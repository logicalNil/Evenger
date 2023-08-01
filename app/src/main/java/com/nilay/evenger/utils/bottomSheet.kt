package com.nilay.evenger.utils

import android.widget.TextView
import androidx.annotation.DrawableRes
import com.nilay.evenger.databinding.LayoutBottomSheetBinding


data class BottomSheetItem(
    val title: String,
    @DrawableRes
    val icon: Int,
    val onIconClick: (() -> Unit)? = null,
    val textViewApply: TextView.() -> Unit = {},
)

fun LayoutBottomSheetBinding.setTopView(model: BottomSheetItem) = this.apply {
    bottomSheetTitle.text = model.title
    btEndButton.setImageResource(model.icon)
    model.onIconClick?.let { btEndButton.setOnClickListener { it() } }
    model.textViewApply(bottomSheetTitle)
}