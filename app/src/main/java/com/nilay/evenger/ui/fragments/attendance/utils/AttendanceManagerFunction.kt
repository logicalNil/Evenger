



package com.nilay.evenger.ui.fragments.attendance.utils

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.nilay.evenger.R
import com.nilay.evenger.core.db.attendance.AttendanceModel
import com.nilay.evenger.core.db.attendance.IsPresent
import com.nilay.evenger.utils.convertLongToTime

fun findPercentage(present: Float, total: Float, action: (Float, Float) -> Float) =
    action(present, total)

fun setResources(percentage: Int, action: (Int) -> Unit) =
    action(percentage)

fun calculatedDays(present: Int, total: Int, action: (Float, Float) -> Float) =
    action(present.toFloat(), total.toFloat())

fun ArrayList<IsPresent>.countTotalClass(size: Int, isPresent: Boolean): Int {
    var days = 1
    val removeIndex = arrayListOf<Int>()
    for ((index, i) in this.withIndex()) {
        if (this.last().day.convertLongToTime("dd/mm/yyyy") == i.day.convertLongToTime("dd/mm/yyyy") && i.isPresent == isPresent) {
            days++
            if (size - 1 != index) {
                removeIndex.add(index)
            }
        }
    }
    for (r in removeIndex.reversed()) {
        this.removeAt(r)
    }
    return days
}

/**
 * @since 4.0.3
 * @author Ayaan
 */
inline fun AttendanceModel.showUndoMessage(
    parentView: View, crossinline action: (AttendanceModel) -> Unit
) = Snackbar.make(
    parentView, "Deleted ${this.subject}", Snackbar.LENGTH_SHORT
).setAction("Undo") {
    action.invoke(this)
}.apply {
    this.setBackgroundTint(
        MaterialColors.getColor(
            parentView.context, com.google.android.material.R.attr.colorSurface, Color.WHITE
        )
    )
    this.setActionTextColor(ContextCompat.getColor(parentView.context, R.color.red))
    this.setTextColor(ContextCompat.getColor(parentView.context, R.color.textColor))
}.show()
