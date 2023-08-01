package com.nilay.evenger.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date


@SuppressLint("SimpleDateFormat")
fun Long.convertToFormatDate(): String {
    val date = java.util.Date(this)
    val format = java.text.SimpleDateFormat("MMMM dd, yyyy")
    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun Long.convertLongToTime(pattern: String): String = SimpleDateFormat(pattern).run {
    val date = Date(this@convertLongToTime)
    this.format(date)
}